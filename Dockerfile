FROM java:8

COPY ./target/typora-custom-script-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar
COPY ./out/artifacts/typora_custom_script_jar/bg-2.png bg2.png

ENTRYPOINT ["sh","-c","java -jar /app.jar bg2.png"]
