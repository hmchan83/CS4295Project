<?
class loginHandler{
	
	function main($para){
		switch($para['action']){
			case 'login':
				return $this->login($para['username'],$para['password']);
			break;
			case 'check':
				return $this->checktoken($para['username'],$para['token']);
			break;
			case 'gcm':
				return $this->gcm($para['username'],$para['gcmid']);
			break;
		}
	}

	function login($username,$password){
		$arr=array();
		$query = $GLOBALS['mysqli']->query('SELECT COUNT(*) FROM user WHERE username = \''.$username.'\' AND password = \''.$password.'\'');
		if(!$query){
			printf("Error: %s\n", $GLOBALS['mysqli']->error);
		}
		$result = $query->fetch_array(MYSQLI_BOTH);
		if($result[0]>0){
			$arr['result']='True';			
			$date = date('Y-m-d h:i:s', time()+3600);
			$query2 = $GLOBALS['mysqli']->query('UPDATE user SET token =\''.$this->createToken().'\', expireday=\''.$date.'\' WHERE  username = \''.$username.'\' AND password = \''.$password.'\'');
			$query = $GLOBALS['mysqli']->query('SELECT uid,username,token FROM user WHERE username = \''.$username.'\' AND password = \''.$password.'\'');
			$results = $query->fetch_array(MYSQLI_ASSOC);
			foreach($results as $key=>$var){
				$arr[$key]=$var;	
			}
		}else{
			
			$arr['result']='False';
		}
		return $arr;
	}
	
	function checktoken($token){// $token will pass to the server with MD5
		$arr=array();
		$date = date('Y-m-d h:i:s', time());
		$query = $GLOBALS['mysqli']->query('SELECT token FROM user WHERE username = \''.$username.'\' ');
		if(!$query){
			printf("Error: %s\n", $GLOBALS['mysqli']->error);
		}
		$result = $query->fetch_array(MYSQLI_BOTH);
		if(md5($result) == $token){
			$arr['result']='True';
		}else{
			$arr['result']='False';
		}
		return $arr;			
	}
	
	function gcm($username,$gcmid){
		$arr = array();
		$query = $GLOBALS['mysqli']->query('UPDATE user SET gcmId =\''.$gcmid.'\' WHERE  username = \''.$username.'\'');
		$arr['result']='True';
		if(!$query){
			$arr['result']='False';
		}
		return $arr;
	}
	
	function createToken() {
   		$PassStr = Array(0,1,2,3,4,5,6,7,8,9,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,A,B,C,D,E,F,G,H,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z);
    	for($i=0;$i<32;$i++) {
			mt_srand((double)microtime()*1000000);
			$randy = mt_rand(0,60);
			$pass .=$PassStr[$randy];
		}
		return $pass;
	}
}
?>