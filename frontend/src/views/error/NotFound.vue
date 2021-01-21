<template>
  <v-card
    class="mx-auto"
    max-width="344"
    outlined
  >
    <v-list-item three-line>
      <v-list-item-content>
        <div class="overline mb-4">404</div>
        <v-list-item-title class="headline mb-1">페이지를 찾을 수 없습니다.</v-list-item-title>
        <v-list-item-subtitle>주소를 다시 확인해 주십시오.</v-list-item-subtitle>
      </v-list-item-content>

      <v-list-item-avatar
        tile
        size="80"
        color="grey"
      ></v-list-item-avatar>
    </v-list-item>

    <v-card-actions>
      <v-btn text color="primary" @click="goPrevious">이전</v-btn>
    </v-card-actions>
  </v-card>
</template>
<script>
export default {
  methods: {
    goPrevious() {
      this.$router.go(-1)
    }
  }
}
</script>

<style>

</style>