<template>
  <v-row
    justify="center"
  >
      <v-col
        class="pa-0"
        lg="4"
        md="4"
        sm="6"
      >
        <Alert
          v-if="alert.alerted"
          :message="alert.message"
          :color="alert.color ? alert.color : 'error'"
        />

        <!-- 뉴스피드 지도 탭, 피드 탭 시작 -->

        <!-- 탭 시작 -->
        <v-tabs v-model="activeTab" grow>
          <v-tabs-slider color="grey"></v-tabs-slider>
          <v-tab 
            v-for="tabItem in tabItems" 
            :key="tabItem.tabId"
          >
            <v-icon>
              {{ tabItem.tabIcon }}

            </v-icon>
          </v-tab>
        </v-tabs>
        <!-- 탭 끝 -->

        <!-- 탭 선택에 따라 보여줄 컴포넌트 -->
        <v-tabs-items 
          touchless
          class="my-0 py-0"
          style="height: 100%;"
          v-model="activeTab">
          <v-tab-item
            v-for="tabItem in tabItems"
            class="my-0 py-0"
            :key="tabItem.tabId"
          >
            <!-- keep-alive 태그를 통해 탭 컴포넌트를 바꿀 때마다 재 생성하는 것이 아닌 데이터를 캐시해두고 다시 볼수있도록 하는 태그 -->
            <keep-alive>
              <component
                v-if="tabItem.content==='Map'" 
                :is="tabItem.content"
                :searchedCenterPosition="centerPosition" 
                @onClick="saveMemory"
                @update="updateCenterPosition"
              ></component>
              <component
                v-else-if="tabItem.content==='Feed'" 
                :is="tabItem.content"
                feed-type="newsfeed"
                :centerPosition="centerPosition"
              ></component>
            </keep-alive>
          </v-tab-item>
        </v-tabs-items>
        <!-- 탭 선택에 따라 보여줄 컴포넌트 끝 -->

   </v-col>
 </v-row>
</template>

<script>
import Map from '@/components/newsfeed/Map.vue' // 지도화면 컴포넌트
import Feed from '@/components/newsfeed/Feed.vue' // 피드화면 컴포넌트
import Alert from '@/components/common/Alert'
import axios from 'axios' // back에 axios 요청을 위한 라이브러리

const SERVER_URL = process.env.VUE_APP_SERVER_URL // 기본 서버 URL 제일 위의 파일보면 .env파일에 해당

export default {
  name: 'NewsFeed',
  components: { // 컴포넌트 등록
    Map, // 지도화면 컴포넌트
    Feed, // 피드화면 컴포넌트
    Alert, // alert 컴포넌트
  },
  data() { // 뉴스 피드에서 사용할 data(변수)들
    return {
      loading: true,
      alert: {
        alerted: false,
        message: '',
        color: '',
      },
      centerPosition: { // 중심좌표를 담을 변수
        lat: '', // 위도
        lng: '', // 경도
      },
      activeTab: "",
      tabItems: [
        { tabId: 0, tabIcon: 'mdi-map-marker', content: 'Map' },
        { tabId: 1, tabIcon: 'mdi-image-multiple', content: 'Feed' }
      ]
    }
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
  // vue가 생성될때 실행되는것들을 담는 곳
  created() {
    // 중심좌표 정보를 담기위한 method를 실행
    this.getCenter()
  },
  methods: {
    saveMemory(res) {    
      this.alert.alerted = false 
      const userId = sessionStorage.getItem('uid') // uid를 저장할 변수 axios요청에 uid가 사용되기 때문에
      
      // 기억 하기 요청 axios 
      // post(url, 기억하기 정보, uid) 가 들어감 
      axios.post(`${SERVER_URL}/users/${userId}/memories`, res, this.getToken)
      .then(()=> {
        this.alert.message = '기억이 완료되었습니다.'
        this.alert.color = 'primary'
        this.alert.alerted = true
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
}
</script>



<style scoped>
</style>