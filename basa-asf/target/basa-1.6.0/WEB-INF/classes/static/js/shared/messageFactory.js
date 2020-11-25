app.factory('messageFactory', function (notifications) {

    function warnAlert(text){
        notifications.showWarning({message: text});
    }
    function successAlert(text){
        notifications.showSuccess({message: text});
    }
    function errorAlert(text){
        notifications.showError({message: text});
    }

    let self = {
        successAlert: function (text) {
            successAlert(text);
        },
        warnAlert: function (text) {
            warnAlert(text);
        },
        errorAlert: function (text) {
            errorAlert(text);
        }
    };
    return self;
});