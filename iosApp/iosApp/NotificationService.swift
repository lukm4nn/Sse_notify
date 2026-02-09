//
// Created by QTI Mac Mini on 09/02/26.
//

import Foundation

import UserNotifications

class NotificationService: UNNotificationServiceExtension {

    override func didReceive(
        _ request: UNNotificationRequest,
        withContentHandler contentHandler: @escaping (UNNotificationContent) -> Void
    ) {

        let userInfo = request.content.userInfo

        if let type = userInfo["type"] as? String,
           type == "WAKE_SSE" {

            SseManager.shared.start()
        }

        contentHandler(request.content)
    }
}

