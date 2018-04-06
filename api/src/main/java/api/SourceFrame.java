package api;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import javax.ws.rs.ApplicationPath;

@OpenAPIDefinition(info = @Info(title = "Budget Studis® API", version = "v1"))
@ApplicationPath("api")
@CrossOrigin(allowOrigin = "http://localhost:3000", supportedMethods = "GET, POST, PUT, DELETE, HEAD, OPTIONS")
public class SourceFrame extends javax.ws.rs.core.Application { }

