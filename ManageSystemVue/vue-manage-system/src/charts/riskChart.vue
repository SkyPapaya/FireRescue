<template>
  <div class="area-chart" ref="chart"></div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts';
import {ref, onMounted} from 'vue';
import {ElNotification} from "element-plus";
import service from "../utils/request";

const chart = ref<HTMLDivElement | null>(null);
let now = new Date();
let data: any[] = [];
let xAxisData: string[] = [];
let option: {
  yAxis: { min: number; max: number; splitLine: { show: boolean }; type: string; boundaryGap: [number, string] };
  xAxis: { data: string[]; splitLine: { show: boolean }; type: string };
  series: { showSymbol: boolean; data: any[]; name: string; type: string }[];
  tooltip: { formatter: (params) => string; axisPointer: { animation: boolean }; trigger: string };
  title: { text: string }
};
let risk = 0.3;

const load = () => {
  // ⬇️ ⬇️ ⬇️ 这是修改点 ⬇️ ⬇️ ⬇️
  // 1. 调用新的、正确的 API
  service.get('environment/getLatestFromAllDevices').then((res) => {

    if (res.code === '200' && res.data && res.data.length > 0) {
      // 2. res.data 现在是一个数组, 包含所有设备的最新数据
      // 我们将遍历这个数组，找到最高的 risk 值
      const riskValues = res.data.map(device => device.risk);
      risk = Math.max(...riskValues); // 3. 将 risk 设置为所有设备中的最大值
    } else {
      // 如果没有数据
      risk = 0;
    }

  }).catch(err => {
    // 捕获 400 之外的其他错误
    console.error("Failed to load risk data:", err);
    risk = 0;
  });
}

  function randomData() {
    now = new Date(now.getTime() + 1000);
    return {
      name: now.toLocaleTimeString(),
      value: [now.toLocaleTimeString(), risk]
    };
  }

  for (var i = 0; i < 100; i++) {
    const newData = randomData();
    data.push(newData.value);
    xAxisData.push(newData.name);
  }

  onMounted(() => {
    const myChart = echarts.init(chart.value!);
    window.addEventListener('resize', () => {
      myChart.resize();
    });

    setInterval(function () {
      load(); // 更新risk数据
      checkFire(); // 检查火灾风险
      let newData = randomData();
      data.shift();
      xAxisData.shift();
      data.push(newData.value);
      xAxisData.push(newData.name);
      myChart.setOption({
        series: [
          {
            data: data
          }
        ],
        xAxis: [
          {
            data: xAxisData
          }
        ]
      });
    }, 1000);

    myChart.setOption(option);
  });

  const checkFire = () => {
    if (risk === 1) {
      ElNotification({
        title: 'Warning ',
        message: '有火灾风险',
        type: 'error',
        duration: 10000,
      });
    }
  };

  option = {
    title: {
      text: '发生火灾的风险'
    },
    tooltip: {
      trigger: 'axis',
      formatter: function (params) {
        params = params[0];
        var date = new Date(params.name);
        return (
            date.getHours() +
            ':' +
            (date.getMinutes()) +
            ':' +
            date.getSeconds() +
            ' ' +
            params.value[1]
        );
      },
      axisPointer: {
        animation: false
      }
    },
    xAxis: {
      type: 'category',
      splitLine: {
        show: true
      },
      data: xAxisData // 不再需要格式化为日期格式
    },
    yAxis: {
      type: 'value',
      boundaryGap: [0, '100%'],
      splitLine: {
        show: false
      },
      min: 0,
      max: 1
    },
    series: [
      {
        name: 'Fake Data',
        type: 'line',
        showSymbol: false,
        data: data
      }
    ]
  };
</script>
