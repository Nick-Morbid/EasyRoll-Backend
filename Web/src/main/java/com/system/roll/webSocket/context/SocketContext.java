package com.system.roll.webSocket.context;

import com.system.roll.webSocket.handler.SocketHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SocketContext {
    private SocketHandler socketHandler;
}
