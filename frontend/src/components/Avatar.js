import React from "react";

const Avatar = ({userId, name, imageUrl, width, height}) => {

    let avatarName = ""
    if(name){
       const splitName = name.split(" ")
        
    }

    return (
        <div className="" style={{width: width+"px", height: height+"px"}}>
            {
                imageUrl ? (
                    <img
                        src= {imageUrl}
                        width={width}
                        height={height}
                        alt={name}
                        className=""
                    />
                ) : (
                    <img
                        src= {require('../assets/default_user_icon.png')}
                        width={width}
                        height={height}
                        alt={name}
                        className=""
                    />
                )
            }

            {
                // online 상태라면 green 불을 주기
            }
        </div>
    )
}

export default Avatar