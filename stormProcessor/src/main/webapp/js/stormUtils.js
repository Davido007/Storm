/**
 * Function shows notification with specific message
 * @param $container    Where You want to put your notification
 * @param message   Message that You want to show in the notification
 * @param type  Type of message: [success|info|warning|danger]
 * @param duration  Duration time
 */
function sendNotification($container, message, type, duration) {
    var $alertBox = $("<div>");
    $alertBox.addClass("alert");

    switch(type) {
        case "success":
            $alertBox.addClass("alert-success");
            break;
        case "info":
            $alertBox.addClass("alert-info");
            break;
        case "warning":
            $alertBox.addClass("alert-warning");
            break;
        case "danger":
            $alertBox.addClass("alert-danger");
            break;
    }

    $alertBox.html(message);
    $container.append($alertBox);
    $alertBox.delay(duration).fadeOut();
}


/**
 * Function shows notification if a table is empty and hides it if not
 * @param $infoPlaceholder  Destination div of notifications
 * @param $table    Table that You want to check
 * @param fieldClass    Class name of single row
 */
function showInfoIfTableIsEmpty($infoPlaceholder, $table, fieldClass) {
    if( $table.find("."+fieldClass).length > 0 ) {
        $infoPlaceholder.css({'visibility':'hidden'});
    } else {
        $infoPlaceholder.css({'visibility':'visible'});
    }
}