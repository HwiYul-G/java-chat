import * as StompJs from '@stomp/stompjs';

const client = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/java-chat',
    connectHeaders: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
    },
    debug: function (str){
        // console.log(str);
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
});

client.onConnect = function (frame) {
    console.log("Connected!");
    if (client.subscriptionRequests) {
        client.subscriptionRequests.forEach(sub => {
            client.subscribe(sub.destination, sub.callback);
        });
        client.subscriptionRequests = [];
    }
};

client.onStompError = function(frame){
    console.log('Broker reported error: ' + frame.headers['message']);
    console.log('Additional details: ' + frame.body);
};

const activateClient = () => {
    if(!client.active)
        client.activate();
};

const deactivateClient = () => {
    if(client.active)
        client.deactivate();
};

const sendGroupMessage = (roomId, message) => {
    client.publish({
        destination: `/pub/group-chat-rooms/${roomId}`,
        body: JSON.stringify(message),
        headers: {
            'content-type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('token')}`
        }
    });
};

const sendPersonalMessage = (roomId, message) =>{
    client.publish({
        destination: `/pub/personal-chat-rooms/${roomId}`,
        body: JSON.stringify(message),
        headers: {
            'content-type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('token')}`
        }
    });
}

const subscribedGroupRooms = new Set();
const subscribedPersonalRooms = new Set();

const subscribeToGroupRoom = (roomId, callback) => {
    const destination = `/sub/group-chat-rooms/${roomId}`;

    if(subscribedGroupRooms.has(roomId))
        return;
    subscribedGroupRooms.add(roomId);

    if (client.connected) {
        client.subscribe(destination, msg => {
            callback(msg);
        });
    } else {
        if (!client.subscriptionRequests) {
            client.subscriptionRequests = [];
        }
        client.subscriptionRequests.push({ destination, callback });
    }
};

const subscribeToPersonalRoom = (roomId, callback) => {
    const destination = `/sub/personal-chat-rooms/${roomId}`;

    if(subscribedPersonalRooms.has(roomId))
        return;
    subscribedPersonalRooms.add(roomId);

    if (client.connected) {
        client.subscribe(destination, msg => {
            callback(msg);
        });
    } else {
        if (!client.subscriptionRequests) {
            client.subscriptionRequests = [];
        }
        client.subscriptionRequests.push({ destination, callback });
    }
};

export {activateClient, deactivateClient, sendGroupMessage, subscribeToGroupRoom, sendPersonalMessage, subscribeToPersonalRoom};