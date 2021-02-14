<template>
    <v-snackbar
    v-model="confirmMessage"
    timeout="-1"
    color="primary"
    bottom
  >
   <span v-html="confirmMessage"></span>
    <template v-slot:action="{ attrs }">
      <v-btn
        v-bind="attrs"
        text
        outlined
        @click="onClick(false)"
      >
        닫기
      </v-btn>
      <v-btn
        v-bind="attrs"
        class="ml-1"
        text
        outlined
        @click="onClick(true)"
      >
        확인
      </v-btn>
    </template>
  </v-snackbar>
</template>

<script>
export default {
  name: 'Confirm',
  props: {
    message: {
      type: String,
    },
  },
  data() {
    return {
      confirmMessage: this.message,
    }
  },
  methods: {
    onClick(confirmed) {
      console.log("??")
      this.$emit('on-confirmed', confirmed)
    }
  }
 }
</script>

<style>

</style>