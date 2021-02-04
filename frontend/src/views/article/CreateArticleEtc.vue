<template>
  <div>
    <v-container>
      <v-row>      
        <v-col cols='12'>
          <validation-provider rules="required" v-slot="{ errors }" >
            <v-textarea
              placeholder="추억을 적어주세요!"
              type="text"
              label="게시 글 입력"
              v-model="contents"
              :error-messages="errors"
              outlined 
            ></v-textarea>
          </validation-provider>
        </v-col>

        <v-col cols='12'>
          <v-combobox
            v-model="hashtags"
            :items="items"
            label="해시태그"
            multiple
            chips
            >
        
            <template v-slot:selection="data">
              <v-chip
                :key="JSON.stringify(data.item)"
                v-bind="data.attrs"
                :input-value="data.selected"
                :disabled="data.disabled"
                @click:close="data.parent.selectItem(data.item)"
              >
                <v-avatar
                  class="accent white--text"
                  left
                  v-text="'#'"
                ></v-avatar>
                {{ data.item }}
              </v-chip>
            </template>
          </v-combobox>
        </v-col>

        <v-col cols='12'>
          <v-menu
            ref="menu"
            v-model="menu"
            :close-on-content-click="false"
            transition="scale-transition"
            offset-y
            min-width="auto"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-text-field
                v-model="date"
                label="날짜 입력"
                prepend-icon="mdi-calendar"
                readonly
                v-bind="attrs"
                v-on="on"
              ></v-text-field>
            </template>
            <v-date-picker
              ref="picker"
              v-model="date"
              :max="new Date().toISOString().substr(0, 10)"
              min="1950-01-01"
              @change="save"
            ></v-date-picker>
          </v-menu>
        </v-col>

        <v-col cols="12">
          <v-btn block @click="onNext" color="primary">다음</v-btn>
        </v-col>
      </v-row>      
    </v-container>
  </div>
</template>

<script>
import { ValidationProvider } from 'vee-validate'

export default {
  components: {
    ValidationProvider
  },
  data() {
    return {
      contents: '',
      items: ['자취방', '덕명동', '2021년', '집 앞'],
      hashtags: '',
      date: null,
      menu: false, 
      articleImages: this.$route.params.articleImages,
      articleInfo: {
        userId: '',
        contents: '',
        hashtags: '',
        lat: '',
        lng: '',
        addressName: '',
        date: '',
      },
    }
  },
  methods: {
    save(date) {
      this.$refs.menu.save(date)
    },
    onNext() {
      this.articleInfo.contents = this.contents
      this.articleInfo.hashtags = this.hashtags
      this.articleInfo.date = this.date
      this.$router.push({name: 'CreateArticleMap', params: {
        articleImages : this.articleImages,
        articleInfo : this.articleInfo
      }})
    }
  },
  watch: {
    menu (val) {
      val && setTimeout(() => (this.$refs.picker.activePicker = 'YEAR'))
    },
  },
}
</script>

<style>

</style>