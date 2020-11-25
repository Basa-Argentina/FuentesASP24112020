app.factory('helpersFactory', function () {
    let self = {
        stringToSnakeCase: function (pString) {
            return pString.toLowerCase().split(" ").join("_");
        },
        stringToDate: function (pString) {
            return new Date(pString);
        },
        longToDate: function (pMilliSeconds) {
            return new Date(pMilliSeconds);
        },
        dateToLong: function (pDate) {
            return pDate.getTime();
        }
    };
    return self;
});