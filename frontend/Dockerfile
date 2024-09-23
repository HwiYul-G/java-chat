# node.js의 경량 버전인 node:alpine을 베이스 이미지로 명세한다.
FROM node:alpine
# 작업 디렉토리를 /app으로 설정한다.
WORKDIR /app
# package.json와 package-lock.json 파일을 복사하고 의존성을 설치한다.
COPY package*.json ./
RUN npm install
# app 파일을 복사, app을 빌드, 3000 포트로 노출한다.
COPY ../../.. .
RUN npm run build
EXPOSE 3000
# 앱 실행
CMD ["npm", "start"]