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
			
			case 'getDetails':
				return $this->getMessgaeDetails($para['teamid'], $para['msgid']);
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
			$handler = new GCMHandler();
			if($replyid==0)
				$handler->sendNewPostMsg($teamid,$uid);
			else
				$handler->sendNewReplyMsg($teamid,$replyid,$uid);
		}
		return $arr;
	}

	function getMessage($teamid, $start, $rows){
		$arr=array();
		$sql = 'SELECT * from message where `teamid`=\''.$teamid.'\' AND `replyid`=\'0\' Order by timestamp DESC LIMIT '.$start.', '.$rows;
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
	function getMessgaeDetails($teamid,$msgid){
		$arr=array();
		$sql = 'SELECT * from message where `teamid`=\''.$teamid.'\' AND `msgid`=\''.$msgid.'\'';
		$sql2 = 'SELECT * from message where `teamid`=\''.$teamid.'\' AND `replyid`=\''.$msgid.'\' Order by timestamp';
		$query = $GLOBALS['mysqli']->query($sql);
		$query2 = $GLOBALS['mysqli']->query($sql2);
		if(!$query){
			printf("Error: %s\n", $GLOBALS['mysqli']->error);
		}else{
			$msg = array();
			while($result = $query->fetch_array(MYSQLI_ASSOC)){
				$msg[]=$result;
			}
			$reply = array();
			while($result = $query2->fetch_array(MYSQLI_ASSOC)){
				$reply[]=$result;
			}
			$arr['msg']=$msg;
			$arr['reply']=$reply;
		}
		return $arr;
	}

}
?>