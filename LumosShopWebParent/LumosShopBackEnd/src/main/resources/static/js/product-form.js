var extraImagesCount = 0;

$(document).ready(function () {





    $("input[name='extraImage']").each(function(index) {
        extraImagesCount++;

        $(this).change(function() {
            if (!checkFileSize(this)) {
                return;
            }
            showExtraImageThumbnail(this, index);
        });
    });

    $("a[name='linkRemoveExtraImage']").each(function(index) {
        $(this).click(function() {
            removeExtraImage(index);
        });
    });


});

function showExtraImageThumbnail(fileInput,index) {
    var file = fileInput.files[0];

    fileName = file.name;

    imageNameHiddenField = $("#imageName" + index);
    if (imageNameHiddenField.length) {
        imageNameHiddenField.val(fileName);
    }
    var reader = new FileReader();
    reader.onload = function (e) {
        $("#extraThumbnail" + index).attr("src", e.target.result);
    };

    reader.readAsDataURL(file);
    if (index >= extraImagesCount - 1) {
        addNextExtraImageSection(index+1);
    }
}

function addNextExtraImageSection(index) {
    let htmlExtraImage = `<div class="col" id="divExtraImage${index}">
                <div class="border m-3 p-2">
                    <div class="d-flex align-items-center justify-content-between mb-5">
                        <div class="fs-4"><label>Extra Image #${index + 1}:</label></div>
                        <div id="extraImageHeader${index}"></div>
                    </div>
                    <div class="m-2">
                        <img id="extraThumbnail${index}" alt="Extra image #${index + 1} preview" class="img-fluid"
                             src="${defaultImageThumbnailSrc}"/>
                    </div>
                    <div>
                        <input type="file" name="extraImage"
                        onchange="showExtraImageThumbnail(this,${index});"
                               accept="image/png, image/jpeg, image/jpg,image/webp"/>
                    </div>
                </div>
            </div>`;

    let htmlLinkRemove = `<a class ="btn fa-duotone fa-times-circle text-danger fa-2x"
                                    title="remove this image"
                                    href="javascript:removeExtraImage(${index - 1})"></a>`
    $("#divProductImages").append(htmlExtraImage);

    $("#extraImageHeader" + (index - 1)).append(htmlLinkRemove);
    extraImagesCount++;

}
function removeExtraImage(index) {
    $("#divExtraImage" + index).remove();
}
