package fw.partnershipservice.feign;

import fw.partnershipservice.model.ResponseMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name="fw-file-service", url="${app.services.file.url}")
public interface FileRestConsumer {

    @PostMapping("file/{partnershipId}")
    ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file,
                                               @PathVariable("partnershipId") Long partnershipId,
                                               @RequestHeader("Authorization") String token);

}
