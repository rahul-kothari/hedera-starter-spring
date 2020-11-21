package hedera.starter.hederaFile;

import com.hedera.hashgraph.sdk.HederaStatusException;
import com.hedera.hashgraph.sdk.file.FileInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api("Handles management of Hedera File Service")
@RequestMapping(path = "/file")
public class FileController {
    /**
     * File Create
     * File Delete
     * File Info
     * TODO: File Update
     * File Append
     * File Content Query
     */

    @Autowired
    FileService fileService;

    public FileController() {
        fileService = new FileService();
    }

    @PostMapping("")
    @ApiOperation("Create a File")
    @ApiImplicitParam(name = "text", type = "String", example = "Adding text to file...")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "File ID")})
    public String createFile(@RequestParam(defaultValue = "") String text) throws HederaStatusException {
        return fileService.createFile(text);
    }

    @DeleteMapping("/{fileId}")
    @ApiOperation("Delete a file")
    @ApiImplicitParam(name = "fileId", required = true, type = "String", example = "0.0.3117")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success or failure")})
    public boolean deleteFile(@PathVariable String fileId) throws HederaStatusException {
        return fileService.deleteFile(fileId);
    }

    @GetMapping("/{fileId}")
    @ApiOperation("Get info on a file")
    @ApiImplicitParam(name = "fileId", required = true, type = "String", example = "0.0.3117")
    public FileInfo getFileInfo(@PathVariable String fileId) throws HederaStatusException {
        return fileService.getFileInfo(fileId);
    }

    @PutMapping("")
    @ApiOperation("Append contents to a file")
    @ApiImplicitParam(name = "fileId", required = true, type = "String", example = "0.0.3117")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileId", required = true, type = "String", example = "0.0.3117"),
            @ApiImplicitParam(name = "text", required = true, type = "String", example = "adding  more content to file...")
    })
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success or failure")})
    public boolean appendFile(@RequestParam String fileId, @RequestParam String text) throws HederaStatusException {
        return fileService.appendToFile(fileId, text);
    }

    @GetMapping("/content/{fileId}")
    @ApiOperation("Get contents on a file")
    @ApiImplicitParam(name = "fileId", required = true, type = "String", example = "0.0.3117")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Content of a file")})
    public String getFileContents(@PathVariable String fileId) throws HederaStatusException {
        return fileService.queryFileContent(fileId);
    }


}
