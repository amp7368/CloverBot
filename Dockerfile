ARG JAVA_VERSION
ARG GRADLE_VERSION

FROM ${GRADLE_VERSION} as build
ARG WORKINGDIR
WORKDIR ${WORKINGDIR}
COPY ./gradle* ./
COPY ./build.gradle .
COPY ./settings.gradle .
COPY ./src ./src

RUN gradle shadowJar --no-daemon

FROM util/secrets as secrets
ARG WORKINGDIR
WORKDIR ${WORKINGDIR}
COPY ./extract.config.sh /secrets/
RUN sh /secrets/extract.sh

FROM ${JAVA_VERSION} as serve
ARG WORKINGDIR
WORKDIR ${WORKINGDIR}
COPY --from=build ${WORKINGDIR}/build/libs/CloverBot* ./
COPY --from=secrets ${WORKINGDIR}/* ./

ENTRYPOINT java -jar CloverBot-2.0-all.jar -o true
