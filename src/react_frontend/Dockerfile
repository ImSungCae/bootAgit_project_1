# Node.js 이미지 사용
FROM node:18

# 작업 디렉토리 설정
WORKDIR /app

# 패키지 종속성 파일 복사
COPY package*.json ./

# 종속성 설치
RUN npm install

# 소스 파일 복사
COPY . .

# 애플리케이션 빌드
RUN npm run build

# 빌드된 애플리케이션을 서빙할 서버 설치
RUN npm install -g serve

# 애플리케이션 포트 설정
EXPOSE 3000

# 애플리케이션 실행
CMD ["serve", "-s", "build"]
