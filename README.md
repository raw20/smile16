# 대학에서 만나요
# 1. 소개
  - 기존 소개팅 어플에서는 상대와 매칭을 하려면 과금을 해야한다. 하지만 대학생들은 금전적으로 여유가 없기 때문에 매칭하기에 부담이 크다. 그리고 대학생들은 소개팅 보다는 과팅에 주로 관심이 많기 때문에 과팅에 초점을 맞춰서 어플을 제작하였다. 또한 대학생들의 금전적인 부담을 덜기 위해서 과금이 아닌 단순히 리워드형 광고를 채택해 사용자가 광고를 시청해 매칭을 위한 코인을 얻을 수 있도록 제작하였다.
  # 2. 어플 구성 Activity
  ## MainActivity
  ![KakaoTalk_20200630_181503725](https://user-images.githubusercontent.com/62588402/86111559-dcae6700-bb01-11ea-9370-d9936fd062eb.png)
  처음에 어플을 실행하면 로그인화면으로 넘어온다. 필요한 정보들을 입력하고 가입하기를 누르면 LginActivity로 넘어간다.
  ## LoginActivity
 ![KakaoTalk_20200630_181503725_01](https://user-images.githubusercontent.com/62588402/86111560-dcae6700-bb01-11ea-97db-fa2b8f40860b.png)
 회원가입 성공후 로그인화면으로 넘어온다.
  ## RegisterActivity
 ![KakaoTalk_20200630_181503725_02](https://user-images.githubusercontent.com/62588402/86111562-dd46fd80-bb01-11ea-91d3-9ad1ae898e33.png)
 로그인이 성공되었으면 유저의 프로필사진과 이메일, 이름을 불러온다. 시작하기 버튼을 누르면 ChatListFragment로 넘어간다.
  ##  ChatListFragment
![KakaoTalk_20200630_181503725_03](https://user-images.githubusercontent.com/62588402/86111564-dd46fd80-bb01-11ea-8cdc-df0e6efe17a9.png)
여기서는 방을 생성할수 있고 생성된 방에 들어갈 수 있다. 그리고 채팅방을 생성하거나 들어가려면 5코인이 필요한데 처음에 사용자는 0코인이 주어진다. 코인을 얻기 위해서는 '코인이 없다면 여기를 누르세요'를 누르면 동영상 광고가 출력되고 광고시청이 끝났으면 10코인이 지급된다.
  ## ChatActivity
![KakaoTalk_20200630_181503725_04](https://user-images.githubusercontent.com/62588402/86111565-dddf9400-bb01-11ea-9f08-e8fd31d5aec5.png)
  ## MapActivity
![KakaoTalk_20200630_181503725_05](https://user-images.githubusercontent.com/62588402/86111554-dae4a380-bb01-11ea-881f-2ac4a778ebeb.png)
  ## ChatActivity
![KakaoTalk_20200630_181503725_06](https://user-images.githubusercontent.com/62588402/86111558-dc15d080-bb01-11ea-9513-f4e3b2cbb427.png)
 ## UserProfileFrgment
![KakaoTalk_20200630_185139547](https://user-images.githubusercontent.com/62588402/86112340-cf45ac80-bb02-11ea-974c-e60256a47b47.png)
  
  


