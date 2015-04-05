<?
class GCMHandler{
	
	function sendNewPostMsg($teamid,$writeruid){//TESTED
		$msg = array('type' => 1, 'teamid' => $teamid);
		$query = $GLOBALS['mysqli']->query('SELECT `gcmId` FROM `user` WHERE uid!=\''.$writeruid.'\' AND uid IN (SELECT uid FROM team_user WHERE teamid = \''.$teamid.'\')');
		$regid = array();
		while($result = $query->fetch_array(MYSQLI_ASSOC)){
			$regid[] = $result['gcmId'];	
		}
		$this->sendNotification($regid,$msg);			
	}
	
	function sendNewReplyMsg($teamid,$msgid,$writeruid){//TESTED
		$msg = array('type' => 2, 'teamid' => $teamid, 'msgid' => $msgid);
		$query = $GLOBALS['mysqli']->query('SELECT `gcmId` FROM `user` WHERE uid IN (SELECT uid FROM message WHERE replyid =\''.$msgid.'\' OR msgid=\''.$msgid.'\') AND uid != '.$writeruid.'');
		$regid = array();
		while($result = $query->fetch_array(MYSQLI_ASSOC)){
			$regid[] = $result['gcmId'];	
		}
		$this->sendNotification($regid,$msg);
	}
	
	function sendAddtoTeamMsg($teamid,$uidarr){//TESTED
		$msg = array('type' => 3, 'teamid' => $teamid);
		$uidstr = implode(',',$uidarr);
		$query = $GLOBALS['mysqli']->query('SELECT `gcmId` FROM `user` WHERE uid IN ('.$uidstr.')');
		$regid = array();
		while($result = $query->fetch_array(MYSQLI_ASSOC)){
			$regid[] = $result['gcmId'];	
		}
		$this->sendNotification($regid,$msg);
	}
	
	function sendRemoveFromTeamMsg($teamid,$uidarr){
		$msg = array('type' => 4, 'teamid' => $teamid);
		$uidstr = implode(',',$uidarr);
		$query = $GLOBALS['mysqli']->query('SELECT `gcmId` FROM `user` WHERE uid IN ('.$uidstr.')');
		$regid = array();
		while($result = $query->fetch_array(MYSQLI_ASSOC)){
			$regid[] = $result['gcmId'];	
		}
		$this->sendNotification($regid,$msg);
	}
	
	function sendSetAsAdminMsg($teamid,$uidarr){
		$msg = array('type' => 5, 'teamid' => $teamid);
		$uidstr = implode(',',$uidarr);
		$query = $GLOBALS['mysqli']->query('SELECT `gcmId` FROM `user` WHERE uid IN ('.$uidstr.')');
		$regid = array();
		while($result = $query->fetch_array(MYSQLI_ASSOC)){
			$regid[] = $result['gcmId'];	
		}
		$this->sendNotification($regid,$msg);
	}
	
	function sendNotification( $registrationIdsArray, $messageData ){   
		$apiKey = "AIzaSyCqXV-EG2CTGcuxrA7_LTrJd0tGdGK69vA";
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
	
}
?>