<template>
  <v-container>
    <v-card-text>
      <Reply v-for="comment in comments" :key="comment.id" :comment="comment" :request_user="request_user" :articleId="articleId" @update-comment="changeComments" @delete-comment="changeComments" />
    </v-card-text>
  </v-container>
</template>

<script>
import Reply from '@/components/reply/Reply'

export default {
  name: 'ReplyList',
  components: {
    Reply
  },
  props: {
    // comments: {
    //   type: Array
    // },
    // articleId: {
    //   type: Number
    // },
    // request_user: {
    //   type: String
    // }
  },
  methods: {
    changeComments() {
      this.$emit('create-comment')
    },
  }

}
</script>

<style>

</style>