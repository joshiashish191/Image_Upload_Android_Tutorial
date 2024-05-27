<?php
$response = array();
if (isset($_FILES['pic']['name'])) {
    define('UPLOAD_PATH', 'user-images/');
    $ext = pathinfo(($_FILES['pic']['name']), PATHINFO_EXTENSION);
    try {
        move_uploaded_file($_FILES['pic']['tmp_name'], UPLOAD_PATH .'myImg.'.$ext);
        $response['error'] = false;
        $response['message'] = 'Image uploaded successfully';
    } catch(Exception $e){
        $response['error'] = true;
        $response['message'] = 'Could not upload image';
    }
} else {
	$response['error'] = true;
	$response['message'] = "Insufficient Parameters";
}
echo json_encode($response);
?>