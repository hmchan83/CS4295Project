<?
class messageHandler{
	
	function main($para){
		switch($para['action']){
			case 'add':
				return $this->addMessage($para['replyid'], $para['teamid'], $para['uid'], $para['msg'],$para['title']);
			break;

			case 'get':
				return $this->getMessage($para['teamid'], $para['start'], $para['rows']);
			break;
		}
	}
	
	function addMessage($replyid, $teamid, $uid, $msg, $title){
		$arr=array();
		$sql = 'INSERT into message (`replyid`, `teamid`,`uid`,`msg`,`title`,`timestamp`) VALUES (\''.$replyid.'\',\''.$teamid.'\', \''.$uid.'\', \''.$msg.'\', \''.$title.'\', NOW())';
		$query = $GLOBALS['mysqli']->query($sql);
		if(!$query){
			printf("Error: %s\n", $GLOBALS['mysqli']->error);
		}else{
			$arr['result'] = 'true';
		}
		return $arr;
	}

	function getMessage($teamid, $start, $rows){
		$arr=array();
		$sql = 'SELECT * from message where `teamid`=\''.$teamid.'\' Order by timestamp DESC LIMIT '.$start.', '.$rows;
		$query = $GLOBALS['mysqli']->query($sql);
		if(!$query){
			printf("Error: %s\n", $GLOBALS['mysqli']->error);
		}else{
			$msg = array();
			while($result = $query->fetch_array(MYSQLI_ASSOC)){
				$msg[]=$result;
			}
			$arr['msg']=$msg;
		}
		return $arr;
	}

}
?>