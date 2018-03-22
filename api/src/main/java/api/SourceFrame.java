package api;

import com.kumuluz.ee.cors.annotations.CrossOrigin;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("api")
@CrossOrigin(allowOrigin = "http://localhost:3000", supportedMethods = "GET, POST, PUT, DELETE, HEAD, OPTIONS")
public class SourceFrame extends javax.ws.rs.core.Application { }

