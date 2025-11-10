<template>
  <div ref="chart"></div>
</template>

<script setup lang="ts">
import {ref, onMounted} from 'vue';
import * as echarts from 'echarts';
import service from "../utils/request";
import {ElNotification} from "element-plus";

const chart = ref<HTMLDivElement | null>(null);
type EChartsOption = echarts.EChartsOption;
let option: {
  yAxis: { min: number; max: number };
  xAxis: { axisLabel: { rotate: number; interval: number }; type: string };
  series: {
    encode: { x: string; y: string };
    datasetIndex: number;
    itemStyle: { color: (params: any) => (string) };
    type: string
  };
  tooltip: { formatter: (params) => string; axisPointer: { type: string }; trigger: string };
  title: { text: string };
  dataset: ({ source: (string | number)[][]; dimensions: string[] } | {
    transform: { type: string; config: { dimension: string; order: string } }
  })[]
} = {
  title: {
    text: '设备数据'
  },
  dataset: [
    {
      dimensions: ['name', 'value'],
      source: [
        ['湿度', 55],
        ['温度', 34.2],
        ['火情', 0],
        ['CO浓度', 0.2],
        ['烟雾浓度', 0.3],
      ]
    },
    {
      // 根据第二个数据进行排序
      transform: {
        type: 'sort',
        config: {dimension: 'value', order: 'desc'}
      }
    }
  ],
  xAxis: {
    type: 'category',
    axisLabel: {interval: 0, rotate: 30}
  },
  yAxis: {
    min: 0,
    max: 100
  },
  // 编码修改为正确的名称
  series: {
    type: 'bar',
    encode: {x: 'name', y: 'value'},
    datasetIndex: 1,
    itemStyle: {
      color: function (params: any) {
        const name = params.data[0]; // 获取名称
        const value = params.data[1]; // 获取 y 值
        if (name === '湿度') {
          return 'blue'; // 湿度柱状图颜色保持蓝色
        } else if (name === '温度' && value > 70) {
          return 'red';
        } else if (value > 45) {
          return 'red';
        } else if (value > 30) {
          return 'yellow';
        } else {
          return 'blue';
        }
        if (name === 'co' && value > 20) {
          return 'red';
        }
        if (name === 'smoke' && value > 30) {
          return 'red';
        }
      }
    }
  },

  tooltip: {
    trigger: 'axis',
    axisPointer: {type: 'shadow'},
    formatter: function (params) {
      const data = params[0].data;
      return `${data[0]}：<br/>${data[1]}`;
    }
  }
};
let humidity = 1;
let temperature;
let fire;
let co;
let smoke;
//获取数据
const load = () => {
  service.get('environment/getLatestFromAllDevices').then((res) => {
    //console.log(res.data[0].co);
    humidity = res.data[0].humidity;
    temperature = res.data[0].temperature;
    fire = res.data[0].fire * 100;
    co = res.data[0].co;
    smoke = res.data[0].smoke;
    // 替换数据

    option.dataset[0].source = [
      ['湿度', humidity],
      ['温度', temperature],
      ['火情', fire],
      ['有害气体浓度', co],
      ['烟雾浓度', smoke],
    ];


  });
}
let type = "火灾类型是：00"
onMounted(() => {
  const myChart = echarts.init(chart.value!);
  load();
  myChart.setOption(option);

  setInterval(() => {
    load();
    // 根据第二个数据排序
    //option.dataset[0].source.sort((a: any, b: any) => b[1] - a[1]);
    // 更新图表
    myChart.setOption(option);
    //弹窗设置
    if (temperature > 50 || fire > 0.01 || co > 20 || smoke > 30) {
      ElNotification({
        title: 'Warning',
        message: '有火情',
        type: 'error',
        duration: 1000
      });
    }
  }, 1000);

});
</script>
