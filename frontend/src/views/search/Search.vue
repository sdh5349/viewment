<template>
<v-container>

  <v-row>
    <v-text-field
    class="mx-2 mt-5"
    rows="1"
    prepend-icon="mdi-magnify"
    @input="onSearch"
    clearable
    >
      <template 
        v-slot:label
      >
        {{searchLabel}}
      </template>
    </v-text-field>
  </v-row>
  <!-- 탭 -->
    <v-tabs
      v-model="onTab"
      grow
    >
      <v-tab
        v-for="tabItem in tabItems"
        :key="tabItem.tabId"
      >
        <v-icon>{{ tabItem.icon }}</v-icon>
      </v-tab>
    </v-tabs>
  <!-- 탭 -->

  <!-- 탭-컴포넌트 -->
    <v-tabs-items
      v-model="onTab"
      continuous
      default
    >
      <v-tab-item
        v-for="tabItem in tabItems"
        :key="tabItem.tabId"
        disabled
      >
        <v-card flat>
          <component 
            v-bind:is="tabItem.content"
            :search=search
          ></component>
        </v-card>
      </v-tab-item>
    </v-tabs-items>
    <!-- 탭-컴포넌트 -->
</v-container>
</template>

<script>
import SearchLocation from '@/components/search/SearchLocation'
import SearchHashtag from '@/components/search/SearchHashtag'
import SearchUser from '@/components/search/SearchUser'
import SearchRecord from '@/components/search/SearchRecord'

export default {
  name: 'Search',
  components: {
    SearchRecord,
    SearchLocation,   
    SearchHashtag,
    SearchUser,   
  },
  data: () => {
    return {
      searchLabel: "Search",
      search: null,
      onTab: null,
      tabItems: [
        { tabId: 0, tabName: 'aMap', icon: 'mdi-map', content: 'SearchLocation', searchLabel: "Map Search"},
        { tabId: 1, tabName: 'Feesdafd', icon: 'mdi-post', content: 'SearchHashtag', searchLabel: "Hashtag Search"},
        { tabId: 2, tabName: 'Useasdfr', icon: 'mdi-account', content: 'SearchUser', searchLabel: "User Search"}
      ],
    }
  },
  methods: {
    onSearch(res) {
      this.search = res
      // console.log(this.search)
    }
  }
}
</script>

<style>

</style>