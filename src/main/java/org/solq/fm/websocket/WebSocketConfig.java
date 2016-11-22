package org.solq.fm.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

//@EnableWebMvc
@EnableWebSocket
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
	// 允许连接的域,只能以http或https开头
	String[] allowsOrigins = { "*" };

	// WebIM WebSocket通道
	registry.addHandler(chatWebSocketHandler(), "/sock").setAllowedOrigins(allowsOrigins).addInterceptors(myInterceptor());
	registry.addHandler(chatWebSocketHandler(), "/sockjs").setAllowedOrigins(allowsOrigins).addInterceptors(myInterceptor()).withSockJS();
    }

    @Bean
    public ChatWebSocketHandler chatWebSocketHandler() {
	return new ChatWebSocketHandler();
    }

    @Bean
    public WebSocketHandshakeInterceptor myInterceptor() {
	return new WebSocketHandshakeInterceptor();
    }

    // public static void main(String[] args) {
    // try {
    // WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    // String uri = "ws://localhost:8081/log/hello/hello/websocket";
    // Session session = container.connectToServer(Client.class, new URI(uri));
    // char lf = 10; // 这个是换行
    // char nl = 0; // 这个是消息结尾的标记，一定要
    // StringBuilder sb = new StringBuilder();
    // sb.append("SEND").append(lf); // 请求的命令策略
    // sb.append("destination:/app/hello").append(lf); // 请求的资源
    // sb.append("content-length:14").append(lf).append(lf); // 消息体的长度
    // sb.append("{\"name\":\"123\"}").append(nl); // 消息体
    //
    // session.getBasicRemote().sendText(sb.toString()); // 发送消息
    // Thread.sleep(50000); // 等待一小会
    // session.close(); // 关闭连接
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
}