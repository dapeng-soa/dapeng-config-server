/**
 * @author huyj
 * @Created  2018/6/9 19:14
 */

indexFormatter = function (value, row, index) {
    return index + 1;
};

showMessage = function (type, mesage, title) {
    switch (type) {
        case 'Success':
            if (title) {
                toastr.success(mesage, title);
            } else {
                toastr.success(mesage);
            }
            break;
        case "Warn":
            if (title) {
                toastr.warning(mesage, title);
            } else {
                toastr.warning(mesage);
            }
            break;
        case "Error":
            if (title) {
                toastr.error(mesage, title);
            } else {
                toastr.error(mesage);
            }
            break;
        case "Info":
            if (title) {
                toastr.info(mesage, title);
            } else {
                toastr.info(mesage);
            }
            break;
        default:
            if (title) {
                toastr.info(mesage, title);
            } else {
                toastr.info(mesage);
            }
    }
}

clearMessage = function () {
    toastr.clear();
}