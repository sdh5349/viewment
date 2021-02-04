<template>
  <v-row
    justify="center"
  >
    <v-col
      lg="4"
      md="4"
      sm="6"
    >
    <!-- 팔로워 리스트, 팔로잉 리스트 탭 시작 -->

    <!-- 탭 시작 -->
    <v-tabs v-model="activeTab" grow>
      <v-tabs-slider color="grey"></v-tabs-slider>
      <v-tab 
        v-for="tabItem in tabItems" 
        :key="tabItem.tabId" 
      >
        {{ tabItem.tabName }}
      </v-tab>
    </v-tabs>
    <!-- 탭 끝 -->

    <!-- 탭 선택에 따라 보여줄 컴포넌트 -->
    <v-tabs-items v-model="activeTab">
      <v-tab-item 
        v-for="tabItem in tabItems" 
        :key="tabItem.tabId"
      >
        <v-card flat class="mt-3">
          <!-- keep-alive 태그를 통해 탭 컴포넌트를 바꿀 때마다 재 생성하는 것이 아닌 데이터를 캐시해두고 다시 볼수있도록 하는 태그 -->
          <!-- TODO: 일단 없이 해보고 필요하다면 사용할 것임 -->
          <keep-alive> 
          <component 
            :is="tabItem.content"
            :profile-user-id="profileUserId"
          ></component>
          </keep-alive>
        </v-card>
      </v-tab-item>
    </v-tabs-items>
    <!-- 탭 선택에 따라 보여줄 컴포넌트 끝 -->

    <!-- 팔로워 리스트, 팔로잉 리스트 탭 끝 -->

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
    profileUserId: String,
    activeTabId: Number,
  },
  data() {
    return {
      loading: true,
      activeTab: "",
      tabItems: [
        { tabId: 0, tabName: 'follower', content: 'FollowerList' },
        { tabId: 1, tabName: 'following', content: 'FollowingList' }
      ]
    }
  },
  created() {
    this.activeTab = this.activeTabId
  },
}
</script>

<style scoped>
</style>