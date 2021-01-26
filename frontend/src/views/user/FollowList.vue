<template>
    <v-row
    justify="center"
  >
    <v-col
      lg="4"
      md="4"
      sm="6"
    >
    <h1>팔로우 리스트!!</h1>
    <!-- 피드, 지도 탭 끝 -->

    <!-- 탭 시작 -->
    <v-tabs v-model="tab" grow>
      <v-tabs-slider color="grey"></v-tabs-slider>
      <v-tab v-for="tabItem in tabItems" :key="tabItem.tabName">
        {{ tabItem.tabName }}
      </v-tab>
    </v-tabs>
    <!-- 탭 끝 -->

    <!-- 탭 선택에 따라 보여줄 컴포넌트 -->
    <v-tabs-items v-model="tab">
      <v-tab-item v-for="tabItem in tabItems" :key="tabItem.tabName">
        <v-card flat>
          <v-card-text>
            <!-- keep-alive 태그를 통해 탭 컴포넌트를 바꿀 때마다 재 생성하는 것이 아닌 데이터를 캐시해두고 다시 볼수있도록 하는 태그 -->
            <!-- TODO: 일단 없이 해보고 필요하다면 사용할 것임 -->
            <!-- <keep-alive>  -->
            <component v-bind:is="tabItem.content"></component>
            <!-- </keep-alive> -->
          </v-card-text>
        </v-card>
      </v-tab-item>
    </v-tabs-items>
    <!-- 탭 선택에 따라 보여줄 컴포넌트 끝 -->

    <!-- 피드, 지도 탭 끝 -->
    </v-col>
  </v-row>
</template>

<script>
import FollowerList from '@/components/user/FollowerList'
import FollowingList from '@/components/user/FollowingList'

export default {
  name: 'FollowList',
  components: {
    FollowerList,
    FollowingList,
  },
  props: {
    profileUserId: String
  },
  data() {
    return {
      tab : null,
      tabItems: [
        { tabName: '팔로워', content: 'FollowerList' },
        { tabName: '팔로잉', content: 'FollowingList' }
      ]
    }
  },
}
</script>

<style>

</style>