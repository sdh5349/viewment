<template>
  <v-row
    class="news-feed-container"
    >
      <v-col
        lg="4"
        md="4"
        sm="6"
      >
      <!-- 뉴스 피드에서 지도기능과 피드기능을 선택할수 있게 만들어 주는 지도탭과 피드탭(시작) -->
        <v-row>   
          <v-tabs 
            fixed-tabs
            color="primary"
          >

          <!-- 뉴스탭(시작)  -->
            <v-col cols='6'>
              <v-tab @click="goMap">
                <v-icon>mdi-map</v-icon>
              </v-tab>
            </v-col>
          <!-- 뉴스탭(끝)  -->

          <!-- 피드탭(시작)  -->
            <v-col cols='6'>
              <v-tab @click="goFeed">
                  <v-icon>mdi-post</v-icon>
              </v-tab>
            </v-col>
          <!-- 피드탭(끝)  -->

          </v-tabs>
        </v-row>
      <!-- 뉴스 피드에서 지도기능과 피드기능을 선택할수 있게 만들어 주는 지도탭과 피드탭(끝) -->


      <!-- 탭 선택에 의해서 나올 지도화면과 피드 화면 (시작) -->
        <v-row class="map-feed-row">

        <v-col>
          
      <!-- 탭 선택에 의해서 나올 지도화면(시작) -->
            <div v-if="view === 0">   
              <Map
                :searchedCenterPosition="centerPosition" 
                @onClick="saveMemory"
                @update="updateCenterPosition"
              />
            </div>
      <!-- 탭 선택에 의해서 나올 지도화면(끝) -->

      <!-- 탭 선택에 의해서 나올 피드 화면 (시작) -->            
            <div v-if="view === 1">
              <Feed 
                feed-type="newsfeed" 
              />
      <!-- 탭 선택에 의해서 나올 피드 화면 (끝) -->   

            </div>
          </v-col>
        </v-row>
      <!-- 탭 선택에 의해서 나올 지도화면과 피드 화면 (끝) -->

   </v-col>
 </v-row>
</template>

<script>
import Map from '@/components/newsfeed/Map.vue' // 지도화면 컴포넌트
import Feed from '@/components/newsfeed/Feed.vue' // 피드화면 컴포넌트
import axios from 'axios' // back에 axios 요청을 위한 라이브러리
const SERVER_URL = process.env.VUE_APP_SERVER_URL // 기본 서버 URL 제일 위의 파일보면 .env파일에 해당

export default {
  components: { // 컴포넌트 등록
    Map, // 지도화면 컴포넌트
    Feed, // 피드화면 컴포넌트
  },
  data() { // 뉴스 피드에서 사용할 data(변수)들
    return {
      view: 0, // 숫자로 지도와 피드를 조절 0은 뉴스 1은 지도 default값을 0(지도)으로 놓음 
      centerPosition: { // 중심좌표를 담을 변수
        lat: '', // 위도
        lng: '', // 경도
      }
    }
  },
  methods: {
    // 지도 화면을 띄워주기 위한 method
    goMap() {
      this.view = 0
    },
    // 피드 화면을 띄워주기 위한 method
    goFeed() {
      this.view = 1
    },

    // 지도 컴포넌트(자식) 에서 emit으로 기억하기에 대한 정보를 넘겨주고
    // 이 method에서 기억하기를 등록해줌
    // res에는 lat(위도), lng(경도), name(기억하기 이름), radius(반경)가 담겨있음
    saveMemory(res) {     
      const userId = sessionStorage.getItem('uid') // uid를 저장할 변수 axios요청에 uid가 사용되기 때문에
      
      // 기억 하기 요청 axios 
      // post(url, 기억하기 정보, uid) 가 들어감 
      axios.post(`${SERVER_URL}/users/${userId}/memories`, res, this.getToken)
      .then(()=> {
        alert('이 장소를 기억했습니다.') // alert로 기억했다고 알려주는 것
        this.$router.go() // 기억 완료했으면 새로고침 해줌
      })
      .catch((err)=> { // 에러가 났을때
        console.log(err) // 에러 출력
      }) 
    },

    // 검색하기의 지도탭에서 검색한 주소의 중심좌표를 얻어오기 위한 method
    // 검색하기에서 router에 params로 담아 처리했음
    getCenter() {
      // 검색하기를 통해 온건지 그냥 뉴스피드버튼을 누른지를 판단하기 위한 조건문
      // 그냥 뉴스피드로 오면 params가 없기 때문에 다음과 같이 처리
      if (this.$route.params){
        // 중심좌표를 담을 변수에 넘어온 정보를 담아줌
        this.centerPosition = {
          lat: this.$route.params.lat, // 위도
          lng: this.$route.params.lng, // 경도
        }
      }
    },
    // 지도 화면 Map.vue(자식)에서 넘어온 중심좌표 정보를 처리하기 위한 method
    // res로 lat(위도)와 lng(경도)가 담겨있는 object가 넘어온다.
    updateCenterPosition(res) {
      // 넘어온 정보를 중심좌표 변수에 할당한다.
      this.centerPosition = res
    } 
  },
  // vue가 생성될때 실행되는것들을 담는 곳
  created() {
    // 중심좌표 정보를 담기위한 method를 실행
    this.getCenter()
  },
  // 어떤 값이 바뀔때 그 바뀐값을 감지하고 어떤 값이나 함수를 return하게 만드는 곳
  computed: {
    // axios요청을할때 uid값을 헤더로 항상 넘겨주는데 그것을 편하게 하기 위한 method
    getToken(){
      const token = sessionStorage.getItem('jwt') // 세션 스토리지에 담겨있는 jwt값을 가져와서 변수에 저장
      
      // 다음의 config라는 변수에 위에서 얻어온 토큰을 저장해 주는데 넘겨줄때 
      // key와 value형태는 팀에서 약속한대로 다음과 같이 해줌
      const config = { 
        headers: {
          'X-Authorization-Firebase': token
        }
      }
      // getToken을 부르면 config값을 리턴해줌
      return config
    }
  },
}
</script>



<style scoped>
/* 스타일 옆에 scoped를 써줬는데 이래야 다른데 영향을 안 준다.*/

/* 뉴스피드 전체를 담을 컨테이너 */
.news-feed-container{
   display: flex; /* flex설정은 가로 정렬을 위한것 block형태 이므로 선언안해주면 한줄한줄 쌓인다.*/
   justify-content: center;  /* 플렉스 요소의 수평 방향 정렬 방식을 설정하는것 */
   align-items: center; /* 플렉스 요소의 수직 방향 정렬 방식을 설정 */
}
.map-feed-row {
  position: relative;
  display: flex; 
  justify-content: center; 
  align-items: center;
  height: 100%;
}
.map-drawer-row {
  position: relative;
  display: flex; 
  justify-content: center; 
  align-items: center;
}

</style>