package cires.bemodule.dev;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DebugController {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public DebugController(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }
    
    @GetMapping("/debug/endpoints")
    public Map<String, String> getEndpoints() {
        Map<String, String> endpoints = new HashMap<>();
        requestMappingHandlerMapping.getHandlerMethods().forEach((key, value) -> {endpoints.put(key.toString(), value.getMethod().getName());});
        return endpoints;
    }
}
