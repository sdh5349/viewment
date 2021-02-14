<template>
  <v-list-item>

    <v-list-item-action>
      <v-row 
        justify="space-between"
        align="center"
      >
        <!-- 사용자 아이콘 / 이름 / 작성 시간이 들어갈 곳! -->
        <UserProfileImage 
          :profileImage="articleInfo.user.profileImage"
          :nickname="articleInfo.user.nickname"
        />
        <!-- 사용자 아이콘 / 이름 / 작성 시간이 들어갈 곳! -->

        <!-- 추천 글이면 추천 표시! -->
        <v-chip
          v-if="articleInfo.recommanded"
          label
          color="yellow lighten-2"
        >
          추천
        </v-chip>
        <!-- 추천 글이면 추천 표시! -->
      </v-row>
    </v-list-item-action>

    <!-- 주소 들어갈 곳! -->
    <v-list-item-text>
      <div>
        <v-icon
          left
        >
          mdi-map-marker
        </v-icon>
        <v-tooltip top>
          <template v-slot:activator="{ on, attrs }">
            <span 
              v-if="articleInfo.pin.addressName"
              v-bind="attrs"
              v-on="on" 
              class="text-body-1"
            >{{ articleInfo.pin.addressName | truncate(10, '...')}}</span>
          </template>
          <span>{{articleInfo.pin.addressName}}</span>
        </v-tooltip>
      </div>
    </v-list-item-text>

    <!-- 사진 들어갈 곳 -->
    <v-list-item-action>
      <v-img
        :src="imageServerPrefix + articleInfo.tumbnail.path"
        :lazy-src="imageServerPrefix + articleInfo.thumbnail.path"
        aspect-ratio="1"
        class="grey lighten-2 image-hover"
        @click="onArticleThumbnail(articleInfo.articleId)"
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
      </v-img>
    </v-list-item-action>
    <!-- 사진 들어갈 곳 -->

    <!-- 해시태그 시작 -->
    <v-card-actions class="pa-0">
      <v-chip-group
        column
      >
        <v-chip
          v-for="(hashtag, i) in articleInfo.hashtags" :key="i"
          class="mx-1 px-2"
          label
          small
          color="primary"
          @click='clickHashtag(hashtag.contents)'
        >
          # {{ hashtag.contents }}
        </v-chip>
      </v-chip-group>
    </v-card-actions>
    <!-- 해시태그 끝 -->
    
  </v-list-item>
</template>

<script>
import UserProfileImage from '@/components/user/UserProfileImage'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'FeedItem',
  components: {
    UserProfileImage
  },
  props: {
    articleInfo: Object
  },
  data() {
    return {
      imageServerPrefix: `${SERVER_URL}/images/`
    }
  },
  methods: {
    onArticleThumbnail(articleId) {
      this.$router.push({ 
        name: 'DetailArticle', 
        params: { articleId }
      })
    }
  }
}
</script>

<style>

</style>