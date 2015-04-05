<?
function sendNotification( $apiKey, $registrationIdsArray, $messageData )
{   
	
    $headers = array("Content-Type:" . "application/json", "Authorization:" . "key=" . $apiKey);
    $data = array(
        'data' => $messageData,
        'registration_ids' => $registrationIdsArray
    );

    $ch = curl_init();
    curl_setopt( $ch, CURLOPT_HTTPHEADER, $headers ); 
    curl_setopt( $ch, CURLOPT_URL, "https://android.googleapis.com/gcm/send" );
    curl_setopt( $ch, CURLOPT_SSL_VERIFYHOST, 0 );
    curl_setopt( $ch, CURLOPT_SSL_VERIFYPEER, 0 );
    curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );
    curl_setopt( $ch, CURLOPT_POSTFIELDS, json_encode($data) );
	echo json_encode($data);
    $response = curl_exec($ch);
    curl_close($ch);

    return $response;
}
?>
<?
// Message to send
$message      = "the test message";
$tickerText   = "ticker text message";
$contentTitle = "content title";
$contentText  = "content body";
$apiKey = "AIzaSyCqXV-EG2CTGcuxrA7_LTrJd0tGdGK69vA";
$registrationId = $_GET['id'];


$response = sendNotification( 
                $apiKey, 
                array("APA91bGXP-rysxtQqWuDN_j8LxP7ffCf9QWnfi4MuON8NksfTtmHKYykp8-FkMuHFhwrNlQH52K--zLLQqPYhRO8Y01p7aHmrAYQlGQiL5Skb6K5Xb7lwdWN7c9D66Q_sSnMBqHGcDjR","APA91bGXP-rysxtQqWuDN_j8LxP7ffCf9QWnfi4MuON8NksfTtmHKYykp8-FkMuHFhwrNlQH52K--zLLQqPYhRO8Y01p7aHmrAYQlGQiL5Skb6K5Xb7lwdWN7c9D66Q_sSnMBqHGcDj"), 
                array('type' => 1, 'teamid' => 1));

echo $response;    
?>