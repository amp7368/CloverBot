ARG NGINX_VERSION

FROM ${NGINX_VERSION} as servestage
COPY ./dist/apps/website /dist/apps/website
COPY ./serve/website/files  /dist/apps/website

RUN rm /etc/nginx/conf.d/default.conf
COPY ./serve/website/default.conf /etc/nginx/conf.d/default.conf
COPY ./serve/website/*.template  /etc/nginx/templates/
