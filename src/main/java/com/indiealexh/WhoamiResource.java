package com.indiealexh;

import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.net.SocketAddress;
import io.vertx.ext.web.RoutingContext;

import jakarta.enterprise.context.ApplicationScoped;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;

@ApplicationScoped
@RouteBase(path = "/")
public class WhoamiResource {

    @Route(path = "", methods = Route.HttpMethod.GET)
    public void whoami(RoutingContext rc) {
        HttpServerRequest request = rc.request();
        StringBuilder response = new StringBuilder();

        // Add hostname
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            response.append("Hostname: ").append(hostname).append("\n");
        } catch (Exception e) {
            response.append("Hostname: unknown\n");
        }

        // Add IP addresses
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    response.append("IP: ").append(address.getHostAddress()).append("\n");
                }
            }
        } catch (SocketException e) {
            response.append("IP: unknown\n");
        }

        // Add remote address
        SocketAddress remoteAddress = request.remoteAddress();
        if (remoteAddress != null) {
            response.append("RemoteAddr: ").append(remoteAddress.host()).append(":").append(remoteAddress.port()).append("\n");
        }

        // Add HTTP request details
        response.append(request.method()).append(" ").append(request.uri()).append(" ").append(request.version()).append("\n");

        // Add headers
        for (Map.Entry<String, String> header : request.headers()) {
            response.append(header.getKey()).append(": ").append(header.getValue()).append("\n");
        }

        // Send response
        rc.response()
            .putHeader("Content-Type", "text/plain")
            .end(response.toString());
    }
}