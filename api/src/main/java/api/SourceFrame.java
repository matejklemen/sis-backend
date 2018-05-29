package api;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

import javax.ws.rs.ApplicationPath;

@OpenAPIDefinition(info = @Info(title = "Budget Studis® API", version = "v1", contact = @Contact, license = @License))
@ApplicationPath("api")
@CrossOrigin(supportedMethods = "GET, POST, PUT, DELETE, HEAD, OPTIONS", exposedHeaders = "X-Total-Count, Content-Disposition, X-Export-Filename")
public class SourceFrame extends javax.ws.rs.core.Application { }

