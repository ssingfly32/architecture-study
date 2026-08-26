import http from 'k6/http';
import { check, sleep } from 'k6';

// 동시 사용자(VU) 수를 단계적으로 늘려가면서, TPS/응답시간이 어디서 무너지는지
// (Saturation Point) 관찰하기 위한 시나리오.
export const options = {
    stages: [
        { duration: '30s', target: 10 },
        { duration: '30s', target: 50 },
        { duration: '30s', target: 100 },
        { duration: '30s', target: 200 },
        { duration: '30s', target: 400 },
        { duration: '30s', target: 0 }, // 정리 구간
    ],
};

export default function () {
    const res = http.get('http://localhost:8080/posts');

    check(res, {
        'status is 200': (r) => r.status === 200,
    });

    sleep(1);
}
