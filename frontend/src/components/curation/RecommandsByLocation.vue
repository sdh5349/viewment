<template>
  <div
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
      lng: 0,
      pins: [],
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
  mounted() {
    const self = this

    if (navigator.geolocation) {
      // GeoLocation을 이용해서 접속 위치를 얻어옵니다
      const success = function (pos) {
        self.lat = pos.coords.latitude
        self.lng = pos.coords.longitude
        self.dataFetch(self.lat, self.lng)
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
      this.lat = 36.3551966493045
      this.lng = 127.298384348217
      this.dataFetch(this.lat, this.lng)
    }
  },
  methods: {
    dataFetch(lat, lng) {
      axios.get(`${SERVER_URL}/pins/fortrend?lat=${this.lat}&lng=${this.lng}`, this.getToken)
      .then(res => {
        window.kakao && window.kakao.maps ? this.initMap(res.data, lat, lng) : this.addKakaoMapScript();
      })
      .then(() => {
        
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
    initMap(pins, lat, lng) {
      const self = this
      console.log("1")

      const currentPosition = new kakao.maps.LatLng(lat, lng) //
      console.log("2")
      const imageSrc = require('@/assets/images/currentPosition.png'); 
      console.log("3")
      const imageSize = new kakao.maps.Size(15, 15); 
      console.log("4")
      const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
      console.log("5")

      // 지도 그리기 시작
      const requestOptions = { 
        center: currentPosition,
        level: 4
      }
      const container = document.getElementById("map")

      console.log(container)
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
      })

      circle.setMap(self.map)
      
      self.createArticlePins(pins) // 게시글 핀을 받아서 지도에 찍기
    },
    createArticlePins(pins) {
      const self = this
      const imageSrc = require('@/assets/images/pin.png'); 
      const imageSize = new kakao.maps.Size(24, 24); 
      const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize); 
      
      // 모든 핀과 핀에 표시할 커스텀오버레이를 생성해서 지도에 표시해줄 순회문
      pins.forEach( pinInfo => {
        // 마커 이미지를 생성합니다 
        var position = new kakao.maps.LatLng(pinInfo.lat, pinInfo.lng)
        
        // 마커를 생성합니다
        var pin = new kakao.maps.Marker({
            map: self.map,
            position: position, // 마커를 표시할 위치
            image : markerImage // 마커 이미지 
        })

        this.pins.push(pin)

        // 커스텀 오버레이 컨텐츠 생성
        var content = document.createElement('input');
        var yAnchor = 1.2
        content.type = 'button'

        if (pinInfo.thumbnail) {
          content.style = `background: url(${self.imageServerPrefix + pinInfo.thumbnail.path}) no-repeat;background-size: 100%; border-radius: 30px; border: none;width: 100px;height: 100px;cursor: pointer;`
        } else {
          content.style = `background-color: #0275db; background-size: 100%; border-radius: 10px; border: none;width: 100px;height: 30px;cursor: pointer; color: 	#FFFFFF; font-size: 12px; font-weight: bold;`
          content.value = '게시글 이동'
          yAnchor = 2
        }
      
        // 커스텀 오버레이 이벤트 생성
        content.addEventListener('click', ()=> {
          self.$router.push({name: 'BindArticle', params: {
            pinId: pinInfo.pinId
          }})
        })

        var overlay = new kakao.maps.CustomOverlay({
          position: position,
          content: content,
          yAnchor: yAnchor,
          clickable: true,
        })

        kakao.maps.event.addListener(pin, 'click', clickPin(self.map, overlay));
        kakao.maps.event.addListener(self.map, 'click', () => {
          overlay.setMap(null)
        })
        
      })

      // 클러스터러 생성
      var clusterer = new kakao.maps.MarkerClusterer({
        map: self.map, // 마커들을 클러스터로 관리하고 표시할 지도 객체 
        averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정 
        minLevel: 4 // 클러스터 할 최소 지도 레벨 
      })

      // 클러스터러 적용
      clusterer.addMarkers(self.pins)

      // 커스텀오버레이를 표시하는 함수입니다 
      function clickPin(map, overlay) {
        return () => {
          if (overlay.getMap()) {
            overlay.setMap(null)
          } else {
            overlay.setMap(map)
          }
        }
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