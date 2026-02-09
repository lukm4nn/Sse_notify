//
// Created by QTI Mac Mini on 09/02/26.
//

import Foundation


class SseManager {

    static let shared = SseManager()
    private var task: URLSessionDataTask?

    func start() {

        let url = URL(string: "https://api.example.com/sse")!
        var request = URLRequest(url: url)
        request.addValue("text/event-stream", forHTTPHeaderField: "Accept")

        task = URLSession.shared.dataTask(with: request)
        task?.resume()
    }

    func stop() {
        task?.cancel()
        task = nil
    }
}


