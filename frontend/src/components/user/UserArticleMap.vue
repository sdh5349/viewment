<template>
  <v-row>
    <v-col>
      <div id="map" class="map"></div>
    </v-col>
      
  </v-row>
</template>

<script>
import axios from 'axios' // back에 axios 요청을 위한 라이브러리
const SERVER_URL = process.env.VUE_APP_SERVER_URL 

export default {
  name: 'UserArticleMap',
  data() {
    return {
      loginUserId: '',
      map: '',
      container: '',
      options: '',
      centerPosition: '',
      addressName : '',
      articlesPins: '',
      pins: [],
      imageServerPrefix: `${SERVER_URL}/images/`,
    }
  },
  props: {
    profileUserId: String,
  },
  mounted() {
    window.kakao && window.kakao.maps
      ? this.initMap() // 참
      : this.addKakaoMapScript(); // 거짓
  },
  methods: {
    // 미리 선언한 kakao가 없을떄 실행하는 method
    addKakaoMapScript() {
      const script = document.createElement("script");
      /* global kakao */
      script.onload = () => kakao.maps.load(this.initMap)
      script.src =
        "http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=41dd8e1c2fab039d8dbbff2e13e8d5a5&libraries=services,clusterer,drawing";
      document.head.appendChild(script);
      
    },
    initMap() {
      
      const self = this

      self.centerPosition = { // 센터 좌표를 직접 설정해준다. 지금은 대전 자취방 좌표
        lat : 36.3586873,
        lng : 127.30278400
      }
      
      self.options = { // 좌표 옵션 센터 + 확대 레벨
          center: new kakao.maps.LatLng(self.centerPosition.lat, self.centerPosition.lng),
          level: 14
      }

      self.container = document.getElementById("map") // 맵을 가져와서 container변수에 넣어줌
      
      self.map = new kakao.maps.Map(self.container, self.options) 



      // 주소를 구하기 위한 부분 (시작)
      var geocoder = new kakao.maps.services.Geocoder() // geocoder는 좌표를 주소로 변환하는 툴
      var callback = function(result, status) { // 이 콜백함수는 result는 주소 결과값 state는 제대로 진행 됬는지
        if (status === kakao.maps.services.Status.OK) { // 진행됬으면 안될때는 좌표이상할떄
            self.addressName = result[0].address_name // 구한 주소값을 주소변수에 넣어준다.
        }
      }
      geocoder.coord2RegionCode(self.centerPosition.lng, self.centerPosition.lat, callback) // 함수 실행
      // 주소를 구하기 위한 부분 (시작)
    },
    getArticlePin() {
      this.loginUserId = sessionStorage.getItem('uid')
      axios.get(`${SERVER_URL}/pins/forusermap?userId=${this.profileUserId}`, this.getToken)
      .then((res)=> {
        console.log(res.data)
        this.articlePins = res.data
        this.createArticlePin()
        
      }) 
    },
    createArticlePin() {
      const self = this
      const imageSrc = 'https://i1.daumcdn.net/dmaps/apis/n_local_blit_04.png'
      const imageSize = new kakao.maps.Size(24, 35)
      const pinImage = new kakao.maps.MarkerImage(imageSrc, imageSize)
      
      self.articlePins.forEach( articlePin => {
        
        var position = new kakao.maps.LatLng(articlePin.lat, articlePin.lng)
        var pin = new kakao.maps.Marker({
            map: self.map, 
            position: position,
            title : articlePin.addressName,
            image : pinImage
        })

        
        var content = document.createElement('input')
        var yAnchor = 1.2
        content.type = 'button'
        content.style = `background: url(${self.imageServerPrefix + articlePin.thumbnail.path}) no-repeat;
                        background-size: 100%; 
                        border-radius: 30px; 
                        border: none;
                        width: 100px;
                        height: 100px;
                        cursor: pointer;`
        

        content.addEventListener('click', function(){
          self.$router.push({name: 'BindArticle', params: {
            pinId: articlePin.pinId,
          }})
        })

        var overlay = new kakao.maps.CustomOverlay({
            position: position,
            content: content,
            yAnchor: yAnchor,
            clickable: true,
        })

        kakao.maps.event.addListener(pin, 'click', clickPin(self.map, overlay))
      
      function clickPin(map, overlay) {   
          return function() {
            
            if (overlay.getMap()) {
              overlay.setMap(null)
            } else {
              overlay.setMap(map)
            }

          }
      }


        self.pins.push(pin)      
      })


      var cluster = new kakao.maps.MarkerClusterer({
        map: self.map,  
        averageCenter: true,
        minLevel: 5 
      })
      cluster.addMarkers(self.pins)

    }
  },
  created() {
    this.getArticlePin()
  },
  computed: {
    getToken(){
      const token = sessionStorage.getItem('jwt')
      const config = {
        headers: {
          'X-Authorization-Firebase': token
        }
      }
      return config
    },
  },
}
</script>

<style scoped>
.map {
  height: 40vh;
}

</style>