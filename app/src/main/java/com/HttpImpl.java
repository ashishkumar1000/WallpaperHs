package com;

import java.io.IOException;

public interface HttpImpl {
    void onHttpFailure(IOException e);

    void onHttpResponse(int requestType, String response);
}
