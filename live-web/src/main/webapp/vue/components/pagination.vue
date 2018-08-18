<template>
    <ul class="mo-paging">
        <!-- prev -->
        <li
                :class="['paging-item', 'paging-item--prev', {'paging-item--disabled' : index === 1}]"
                @click="prev">prev</li>

        <!-- first -->
        <li
                :class="['paging-item', 'paging-item--first', {'paging-item--disabled' : index === 1}]"
                @click="first">first</li>

        <li
                :class="['paging-item', 'paging-item--more']"
                v-if="showPrevMore">...</li>

        <li
                :class="['paging-item', {'paging-item--current' : index === pager}]"  //index是当前页码
        v-for="pager in pagers"
        @click="go(pager)">{{ pager }}</li>

        <li
                :class="['paging-item', 'paging-item--more']"
                v-if="showNextMore">...</li>

        <!-- last -->
        <li
                :class="['paging-item', 'paging-item--last', {'paging-item--disabled' : index === pages}]"
                @click="last">last</li>

        <!-- next -->
        <li
                :class="['paging-item', 'paging-item--next', {'paging-item--disabled' : index === pages}]"
                @click="next">next</li>
    </ul>

</template>

<script type="es6">
    export default {
        name: "pagination"
    }
</script>

<style scoped>

</style>