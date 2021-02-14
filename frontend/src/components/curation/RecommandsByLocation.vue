<template>
  <v-row
    v-if="loading"
    style="height: 50vh;"
    class="fill-height ma-0"
    align="center"
    justify="center"
  >
    <v-progress-circular
      indeterminate
      color="primary"
    ></v-progress-circular>
  </v-row>
  <div
    v-else
    id="map"
    class="map"
  >
  </div>


</template>

<script>
import axios from 'axios'
const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'RecommandsByLocation',
  data() {
    return {
      loading: true,
      imageServerPrefix: `${SERVER_URL}/images/`,
      map: null,
      lat: 0,
      lon: 0,
    }
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
    }
  },
  created() {
    },
  mounted() {
    this.dataFetch()
  },
  methods: {
    dataFetch() {
      axios.get(`${SERVER_URL}/pins`, this.getToken)
      .then(res => {
        window.kakao && window.kakao.maps ? this.initMap(res.data) : this.addKakaoMapScript();
      })
      .then(() => {
        this.loading = false
      })
      .catch(err => {
      })
    },
    addKakaoMapScript() {
      const script = document.createElement("script");
      /* global kakao */
      script.onload = () => kakao.maps.load(this.initMap)
      script.src =
        "http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=41dd8e1c2fab039d8dbbff2e13e8d5a5";
      document.head.appendChild(script);
    },
    initMap(pins) {
      const self = this
      // 사용자의 현재 위치를 기반으로 맵 보여준다.
      if (navigator.geolocation) {
        
        // GeoLocation을 이용해서 접속 위치를 얻어옵니다
        const success = function (pos) {

          const currentPosition = new kakao.maps.LatLng(pos.coords.latitude, pos.coords.longitude) //
          const imageSrc = require('@/assets/images/currentPosition.png'); 
          const imageSize = new kakao.maps.Size(15, 15); 
          const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

          // 지도 그리기 시작
          const requestOptions = { 
            center: currentPosition,
            level: 4
          }
          const container = document.getElementById("map")
          self.map = new kakao.maps.Map(container, requestOptions)
          // 지도 그리기 끝

          // 현재 위치 표시 시작
          var currentPin = new kakao.maps.Marker({
            position: currentPosition, // 마커를 표시할 위치
            image : markerImage // 마커 이미지 
          })
          // 현재 위치 표시 끝

          currentPin.setMap(self.map)

          var circle = new kakao.maps.Circle({
              center : currentPosition,  // 원의 중심좌표 입니다 
              radius: 1500, // 미터 단위의 원의 반지름입니다 
              fillColor: '#CFE7FF', // 채우기 색깔입니다
              strokeWeight: 1,
              fillOpacity: 0.2  // 채우기 불투명도 입니다  
          }); 

          console.log(circle)

          circle.setMap(self.map)
          
          self.createArticlePins(pins) // 게시글 핀을 받아서 지도에 찍기
        }

        const error = function (err) {
          console.warn('ERROR(' + err.code + '): ' + err.message);
        }

        navigator.geolocation.getCurrentPosition(success, error, {
          enableHighAccuracy: true,
          timeout: 5000,
          maximumAge: 0
        })
      } else { // HTML5의 GeoLocation을 사용할 수 없을때 마커 표시 위치와 인포윈도우 내용을 설정합니다
        const options = { 
          center: new kakao.maps.LatLng(36.3551966493045, 127.298384348217),
          level: 4
        }
        const container = document.getElementById("map")
        self.map = new kakao.maps.Map(container, options)
        self.createArticlePins(pins)
      }
    },
    createArticlePins(pins) {
      const self = this
      const imageSrc = require('@/assets/images/pin.png'); 
      const imageSize = new kakao.maps.Size(24, 24); 
      const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize); 
      
      pins.forEach( pinInfo => {
        // 마커 이미지를 생성합니다 
        var position = new kakao.maps.LatLng(pinInfo.lat, pinInfo.lng)
        
        // 마커를 생성합니다
        var pin = new kakao.maps.Marker({
            map: self.map,
            position: position, // 마커를 표시할 위치
            image : markerImage // 마커 이미지 
        })

        var content = document.createElement('input');
        var yAnchor = 1.2
        content.type = 'button'
        content.addEventListener('click', function(){
        })

        if (pinInfo.thumbnail) {
          content.style = `background: url(${self.imageServerPrefix + pinInfo.thumbnail.path}) no-repeat;background-size: 100%; border-radius: 30px; border: none;width: 100px;height: 100px;cursor: pointer;`
				
        } else {
          content.style = `background-color: #0275db; background-size: 100%; border-radius: 10px; border: none;width: 100px;height: 30px;cursor: pointer; color: 	#FFFFFF; font-size: 12px; font-weight: bold;`
          content.value = '게시글 이동'
          yAnchor = 2
        }
      

        var overlay = new kakao.maps.CustomOverlay({
            position: position,
            content: content,
            yAnchor: yAnchor,
            clickable: true,
        })

        kakao.maps.event.addListener(pin, 'click', clickPin(self.map, overlay));
        kakao.maps.event.addListener(self.map, 'click', function(mouseEvent) {
          overlay.setMap(null)
       });
        
        function clickOverlay() {
           console.log("오버레이클릭하셨음!, "  )   
        }

      })

      // 커스텀오버레이를 표시하는 클로저를 만드는 함수입니다 
      function clickPin(map, overlay) {
          return function() {
            console.log(overlay.getMap())
            if (overlay.getMap()) {
              overlay.setMap(null)
              console.log("닫힘")
            } else {
              console.log(overlay)
              overlay.setMap(map)
              console.log("열림")
            }

          };
      }
    },
  },
}
</script>

<style scoped>
  .map {
    width: 100%;
    height: 15rem;
    z-index: 0;
  }
</style>