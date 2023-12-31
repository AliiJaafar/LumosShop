package com.dgpad.admin.product;

import com.dgpad.admin.service.CategoryService;
import com.dgpad.admin.user.UserNotFoundException;
import com.dgpad.admin.user.UserService;
import com.dgpad.admin.util.B2_Util;
import com.dgpad.admin.util.FileUploadUtil;
import com.lumosshop.common.entity.Category;
import com.lumosshop.common.entity.User;
import com.lumosshop.common.entity.product.Product;
import com.lumosshop.common.entity.product.ProductImage;
import com.lumosshop.common.exception.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class productController {
    private static final Logger LOGGER = LoggerFactory.getLogger(productController.class);


    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;


    @GetMapping("/products")
    public String listAll(Model model) {

        return ProductByPage(1, model, "id", null, "asc", null);
    }

    @GetMapping("/products/page/{pageNum}")
    public String ProductByPage(
            @PathVariable(name = "pageNum") int pageNum,
            Model model,
            @Param("sortField") String sortField,
            @Param("keyword") String keyword,
            @Param("sortDirection") String sortDirection,
            @Param("categoryId") Integer categoryId
    ) {

        if (categoryId != null) {
            model.addAttribute("categoryId", categoryId);
        }
        Page<Product> productPage = productService.listByPage(pageNum, sortField, sortDirection, keyword,categoryId);

        List<Product> productList = productPage.getContent();


        List<Category> categoryList = categoryService.listCategoriesUsedInForm();




        long startCount = (pageNum - 1) * productService.PRODUCT_PER_PAGE + 1;
        long endCount = startCount + productService.PRODUCT_PER_PAGE - 1;
        if (endCount > productPage.getTotalElements()) {
            endCount = productPage.getTotalElements();
        }
        String reverseSortDirection = sortDirection.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalElement", productPage.getTotalElements());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", reverseSortDirection);
        model.addAttribute("keyword", keyword);
        model.addAttribute("moduleURL", "/products");
        model.addAttribute("productList", productList);
        model.addAttribute("categoryList", categoryList);

        System.out.println("StartCount: " + startCount);
        System.out.println("endCount: " + endCount);
        System.out.println("totalElement: " + productPage.getTotalElements());
        System.out.println("productList size: " + productList.size());

        return "products/products";
    }

    @GetMapping("/products/add")
    public String addProduct(Model model) {


        List<Category> categoryList = categoryService.findAll();
        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("product", product);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("pageTitle", "Create New Product");
        model.addAttribute("numberOfExistingExtraImages", 0);


        return "products/products-form";
    }

    @PostMapping("/products/save")
    public String saveTheProduct(Product product,
                                 RedirectAttributes redirectAttributes,
                                 @RequestParam(name = "fileImage", required = false) MultipartFile mainImageMultiPart,
                                 @RequestParam(name = "extraImage", required = false) MultipartFile[] extraImageMultiPart,
                                 @RequestParam(name = "detailIDs", required = false) String[] detailIDs,
                                 @RequestParam(name = "detailNames", required = false) String[] detailNames,
                                 @RequestParam(name = "detailValues", required = false) String[] detailValues,
                                 @RequestParam(name = "imageIDs", required = false) String[] imageIDs,
                                 @RequestParam(name = "imageNames", required = false) String[] imageNames
    ) throws IOException {
        setMainImageName(mainImageMultiPart, product);
        setExistingExtraImageNames(imageIDs, imageNames, product);
        setNewExtraImageNames(extraImageMultiPart, product);
        setProductDetails(detailIDs, detailNames, detailValues, product);


//        removeUnusedExtraImagesFromForm(product);
        removeUnusedExtraImagesFromForm_B2(product);

        Product savedProduct = productService.save(product);

//        saveUploadedImages(mainImageMultiPart, extraImageMultiPart, savedProduct);

        saveUploadedImagesB2(mainImageMultiPart, extraImageMultiPart, savedProduct);
        redirectAttributes.addFlashAttribute("message", "The product has been saved without any issues.");
        return "redirect:/products";
    }

    private void removeUnusedExtraImagesFromForm(Product product) {

        String ImageLocate = "product-images/" + product.getId() + "/extras";
        Path pathDirectory = Paths.get(ImageLocate);

        try {
            Files.list(pathDirectory).forEach(file -> {

                String fileName = file.toFile().getName();
                if (!product.containsImageName(fileName)) {
                    try {
                        Files.delete(file);
                        LOGGER.info("Deleted Image which is an extra: " + fileName);

                    } catch (IOException e) {

                        LOGGER.error("Could not delete This extra Image: " + fileName);
                    }
                }
            });
        } catch (IOException e) {
            LOGGER.error("Could not list Directory : " + pathDirectory);

        }
    }

/*    private void removeUnusedExtraImagesFromForm_B2(Product product) {
        String extraImageDirectory = "product-images/" + product.getId() + "/extras";
        List<String> listKeys = B2_Util.listDir(extraImageDirectory);

        for (String objectKey : listKeys) {
            int lastIndexOfSlash = objectKey.lastIndexOf("/");
            String fileName = objectKey.substring(lastIndexOfSlash + 1, objectKey.length());

            if (!product.containsImageName(fileName)) {
                B2_Util.deleteFile(objectKey);
            }
        }
    }*/

    private void removeUnusedExtraImagesFromForm_B2(Product product) {
        String extraImageDirectory = "product-images/" + product.getId() + "/extras";
        List<String> listKeys = B2_Util.listDir(extraImageDirectory);

        listKeys.stream()
                .filter(objectKey -> isUnusedImage(product, objectKey))
                .forEach(B2_Util::deleteFile);
    }

    private boolean isUnusedImage(Product product, String objectKey) {
        String fileName = extractFileName(objectKey);
        return !product.containsImageName(fileName);
    }

    private String extractFileName(String objectKey) {
        Path path = Paths.get(objectKey);
        return path.getFileName().toString();
    }

    private void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {
        if (imageIDs == null || imageIDs.length == 0) return;

        Set<ProductImage> images = new HashSet<>();

        for (int count = 0; count < imageIDs.length; count++) {
            Integer id = Integer.parseInt(imageIDs[count]);
            String name = imageNames[count];

            images.add(new ProductImage(id, name, product));
        }

        product.setImages(images);
    }

    private void setProductDetails(String[] detailIDs, String[] detailNames, String[] detailValues, Product product) {
        if (detailNames == null || detailNames.length == 0) return;
        for (int count = 0; count < detailNames.length; count++) {
            String name = detailNames[count];
            String value = detailValues[count];
            Integer id = Integer.parseInt(detailIDs[count]);


            if (id != 0) {
                product.addDetails(id, name, value);
            } else if (!name.isEmpty() && !value.isEmpty()) {
                product.addDetails(name, value);
            }
        }
    }

    private void saveUploadedImages(MultipartFile mainImageMultiPart,
                                    MultipartFile[] extraImageMultiPart,
                                    Product savedProduct) throws IOException {

        if (!mainImageMultiPart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultiPart.getOriginalFilename());
            String uploadDir = "product-images/" + savedProduct.getId();


            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultiPart);

        }
        if (extraImageMultiPart.length > 0) {
            String uploadDir = "product-images/" + savedProduct.getId() + "/extras";


            for (MultipartFile multipartFile : extraImageMultiPart) {
                if (multipartFile.isEmpty()) continue;

                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

            }
        }

    }

/*    private void saveUploadedImagesB2(MultipartFile mainImageMultiPart,
                                      MultipartFile[] extraImagesMultiPart,
                                      Product product) throws IOException {
        if (!mainImageMultiPart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultiPart.getOriginalFilename());
            String uploadDirectory = "product-images/" + product.getId();

            List<String> keyList = B2_Util.listDir(uploadDirectory + "/");
            for (String object : keyList) {
                if (!object.contains("/extras/")) {
                    B2_Util.deleteFile(object);
                }
            }

            B2_Util.uploadFile(uploadDirectory, fileName, mainImageMultiPart.getInputStream());
        }
        if (extraImagesMultiPart.length > 0) {
            String uploadDirectory = "product-images/" + product.getId() + "/extras";


            for (MultipartFile multipartFile : extraImagesMultiPart) {
                if (multipartFile.isEmpty()) {
                    continue;
                }
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

                B2_Util.uploadFile(uploadDirectory, fileName, multipartFile.getInputStream());
            }
        }

    }*/

    private void saveUploadedImagesB2(MultipartFile mainImageMultiPart,
                                      MultipartFile[] extraImagesMultiPart,
                                      Product product) throws IOException {
        processMainImage(mainImageMultiPart, product);
        processExtraImages(extraImagesMultiPart, product);
    }

    private void processMainImage(MultipartFile mainImageMultiPart, Product product) throws IOException {
        if (!mainImageMultiPart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultiPart.getOriginalFilename());
            String uploadDirectory = "product-images/" + product.getId();

            deleteNonExtrasImages(uploadDirectory);
            B2_Util.uploadFile(uploadDirectory, fileName, mainImageMultiPart.getInputStream());
        }
    }

    private void processExtraImages(MultipartFile[] extraImagesMultiPart, Product product) throws IOException {
        if (extraImagesMultiPart.length > 0) {
            String uploadDirectory = "product-images/" + product.getId() + "/extras";

            for (MultipartFile multipartFile : extraImagesMultiPart) {
                if (!multipartFile.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    B2_Util.uploadFile(uploadDirectory, fileName, multipartFile.getInputStream());
                }
            }
        }
    }

    private void deleteNonExtrasImages(String uploadDirectory) {
        List<String> keyList = B2_Util.listDir(uploadDirectory + "/");
        keyList.stream()
                .filter(object -> !object.contains("/extras/"))
                .forEach(B2_Util::deleteFile);
    }


    private void setNewExtraImageNames(MultipartFile[] extraImageMultiPart, Product product) {
        if (extraImageMultiPart.length > 0) {
            for (MultipartFile multipartFile : extraImageMultiPart) {
                if (!multipartFile.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    if (!product.containsImageName(fileName)) product.addExtraImage(fileName);


                }
            }
        }
    }


    private void setMainImageName(MultipartFile mainImageMultiPart, Product product) {
        if (!mainImageMultiPart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultiPart.getOriginalFilename());
            product.setMainImage(fileName);
        }
    }

    @GetMapping("/products/edit/{id}")
    public String showFormForUpdate(@PathVariable(name = "id") int theId,
                                    Model theModel,
                                    RedirectAttributes redirectAttributes) {
        try {
            List<Category> categoryList = categoryService.findAll();
            Product product = productService.findProductById(theId);
            Integer numberOfExistingExtraImages = product.getImages().size();

            theModel.addAttribute("categoryList", categoryList);
            theModel.addAttribute("product", product);
            theModel.addAttribute("pageTitle", "Edit Product ID : ( " + theId + " )");
            theModel.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);


            // send over to our form
            return "products/products-form";
        } catch (ProductNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/products/{id}/enabled/{status}")
    public String UpdateEnableStatus(@PathVariable(name = "id") Integer id,
                                     @PathVariable(name = "status") boolean enabled,
                                     RedirectAttributes redirectAttributes) {

        productService.updateProductEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "We've updated the status for Product ID " + id + " to " + status + ".";
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/products";

    }

    @GetMapping("/products/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id,
                         Model model,
                         RedirectAttributes redirectAttributes) throws UserNotFoundException {
        try {
            productService.deleteById(id);
            String productExtraImageDir = "product-images/" + id + "/extras";
            String productImageDir = "product-images/" + id;

            /*FileUploadUtil.removeDir(productExtraImageDir);
            FileUploadUtil.removeDir(productImageDir);*/

            B2_Util.removeFolder(productExtraImageDir);
            B2_Util.removeFolder(productImageDir);

            redirectAttributes.addFlashAttribute("message", "Product ID " + id + " has been deleted from the system with success.");
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        // redirect to
        return "redirect:/products";
    }

    @GetMapping("/products/info/{id}")
    public String viewProductInformation(@PathVariable("id") Integer id, Model model,
                                         RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.findProductById(id);
            model.addAttribute("product", product);
            return "products/product_modal";
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/products";
        }
    }
}
