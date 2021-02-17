<template>
  <v-row 
    class="scroll-container"
  >
    <v-col
      v-for="article in articlesInfo"
      :key="article.articleId"
      class="d-flex child-flex grid-item-padding"
      cols="4"
    >
      <!-- 마우스 hover 시 위치 정보를 알려줌 PC에서만 작동 시작 -->
      <v-hover v-slot="{ hover }">
        <v-scale-transition>
          <v-img
            :src="imageServerPrefix + article.thumbnail.path"
            :lazy-src="imageServerPrefix + article.thumbnail.path"
            aspect-ratio="1"
            class="grey lighten-2 image-hover"
            @click="onArticleThumbnail(article.articleId)"
          >

            <!-- 이미지 요청이 길어지면 뜨는 로딩 창 시작 -->
            <template v-slot:placeholder>
              <v-row
                class="fill-height ma-0"
                align="center"
                justify="center"
              >
                <v-progress-circular
                  indeterminate
                  color="grey lighten-5"
                ></v-progress-circular>
              </v-row>
            </template>
            <!-- 이미지 요청이 길어지면 뜨는 로딩 창 끝 -->

            <!-- 이미지에 위치 정보를 나타내기 위한 트랜지션 시작 -->
            <v-expand-transition>
              <div
                v-if="hover"
                class="d-flex black lighten-2 reveal white--text text-center"
                style="height: 100%; font-size: 1.2rem;"
              >
                <span style="white-space: pre-line">{{ article.pin.addressName | truncate(20, '...') }}</span>
              </div>
            </v-expand-transition>
            <!-- 이미지에 위치 정보를 나타내기 위한 트랜지션 끝 -->

          </v-img>
        </v-scale-transition>
      </v-hover>
      <!-- 마우스 hover 시 위치 정보를 알려줌 PC에서만 작동 끝 -->
      
    </v-col>
  </v-row>
</template>

<script>
import axios from 'axios'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'UserArticleGrid',
  // reveal 창에 나타날 지역 명칭을 필터링
  filters: {
    truncate: function (text, length, suffix) {
      if (text && text.length > length) {
        const textArray = text.substring(0, length).split(' ')
        // 20자가 넘으면 공백 기준으로 단어를 자른후 마지막 인덱스만 버린채 문자열로 보여준다.
        return textArray.slice(0,-1).join('\n')
      } else {
        return text;
      }
    },
  },
  props: {
    profileUserId: String,
  },
  data() {
    return { 
      articlesInfo: [],
      imageServerPrefix: `${SERVER_URL}/images/`,
      page: 0,
      size: 15,
    }
  },
  computed: {
    getToken() {
      const token = sessionStorage.getItem('jwt')

      const config = {
        headers: {
          'X-Authorization-Firebase': token
        }
      }
      return config
    }
  },
  created () {
    window.addEventListener('scroll', this.handleScroll);
    this.readMore()
  },
  destroyed () {
    window.removeEventListener('scroll', this.handleScroll);
  },
  methods: {
    readMore() {
      axios.get(`${SERVER_URL}/articles/searchbyuserid/${this.profileUserId}/pg?page=${this.page}&size=${this.size}`, this.getToken)
      .then(res => {
        this.articlesInfo.push(...res.data.content)
        this.page += 1
        this.last = res.data.last
      })
      .catch(err => {
      })
    },
    onArticleThumbnail(articleId) {
      this.$router.push({ 
        name: 'DetailArticle', 
        params: {
          articleId,
        }
      })
    },
    handleScroll() {
      if (Math.round(document.documentElement.scrollTop) + window.innerHeight === document.documentElement.offsetHeight && !this.last) {
        this.readMore() 
      }
    }
  }
}
</script>

<style scoped>
/* 그리드 각 아이템 패딩 설정 */
.grid-item-padding {
  padding: 0.3rem;
}

/* 스크롤 컨테이너 안의 아이템이 넘쳐도 스크롤 컨테이너의 크기는 고정 */
.scroll-container {
  height: 100%;
  overflow: hidden;
  margin-bottom: 50px;
}

/* 마우스 호버 시 커서 포인터로 전환 */
.image-hover:hover {
  cursor: pointer;
}

/* 반투명한 reveal 트랜지션 */
.reveal {
  align-items: center;
  justify-content: center;
  bottom: 0;
  opacity: .4;
  position: absolute;
  width: 100%;
}
</style>