package ru.binarshiki.binmedia.controller;

import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("object")
@RequiredArgsConstructor
@Slf4j
public class TextController {

    @Autowired
    private final MinioAsyncClient minioClient;

    private final Path path = Paths.get("./files");

//    @GetMapping("save")
//    public Mono<?> getObject(@RequestParam("name") String object) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
//        CompletableFuture<GetObjectResponse> inputStream = minioClient.getObject(GetObjectArgs.builder()
//                .bucket("simplebucket")
//                .object(object)
//                .build());
//        try {
//            return Mono.just(inputStream.get());
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @GetMapping("stream")
    public Mono<InputStreamResource> download(@RequestParam(name = "name")String name) {
        return Mono.fromCallable(() -> {
            CompletableFuture<GetObjectResponse> response = minioClient.getObject(GetObjectArgs.builder().bucket("simplebucket").object(name).build());
            return new InputStreamResource(response.get());
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping("add")
    public Mono<?> addObject(@RequestPart("file") Mono<FilePart> filePart)  {
        return filePart.doOnNext(fp -> log.info("Name: {}", fp.filename()))
                .publishOn(Schedulers.boundedElastic())
                .flatMap(fp -> {
                            try {
                                File temp = new File(fp.filename());
//                                log.info("Filename: {}", temp.getAbsolutePath());
                                fp.transferTo(temp).block();
                                UploadObjectArgs uploadObject = UploadObjectArgs.builder()
                                        .filename(temp.getName())
                                        .object(fp.filename())
                                        .bucket("simplebucket")
                                        .build();
//                                log.info("UploadObject: {}", uploadObject.bucket());
//                                log.info("UploadObject: {}", uploadObject.object());
//                                log.info("UploadObject: {}", uploadObject.filename());
                                minioClient.uploadObject(uploadObject);
                            } catch (IOException | InsufficientDataException |
                                     InternalException | InvalidKeyException |
                                     NoSuchAlgorithmException | XmlParserException e) {
                                return Mono.error(new RuntimeException(e));
                            }
                            return Mono.just(HttpStatus.OK);
                        }).then();
    }
}