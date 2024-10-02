import React, { useState, createRef } from "react";
import {
    View,
    StyleSheet,
    TextInput,
    Text,
    KeyboardAvoidingView,
} from "react-native";
import theme from "../../assets/Theme";
import BackButton from "./Components/BackBtn";
import NextButton from "./Components/NextBtn";
import SkipButton from "./Components/SkipBtn";
import Indicator from "./Components/Indicator";

function InitUserInfo() {
    const inputRef = createRef();
    const [step, setStep] = useState(0);
    const [activated, setActivated] = useState(false);
    const [userinfo, setUserinfo] = useState({
        id: "",
        name: "",
        intro: "",
        profileUrl: "",
    });
    const [input, setInput] = useState("");
    const guideSet = [
        "아이디를 입력해주세요",
        "이름을 알려주세요",
        "한 줄 소개를 입력해주세요",
        "프로필 사진을 선택해주세요",
    ];
    const placeholderSet = ["아이디", "이름", "소개"];
    const onChange = (event) => {
        switch (step) {
            case 0:
            case 1:
            case 2:
                let text = event.nativeEvent.text.trim();
                setInput(text);
                if (text.length > 0) {
                    setActivated(true);
                } else {
                    setActivated(false);
                }
                break;
        }
    };
    const onPress = () => {
        if (step < 3) {
            switch (step) {
                case 0:
                    setUserinfo({
                        ...userinfo,
                        id: input,
                    });
                    break;
                case 1:
                    setUserinfo({
                        ...userinfo,
                        name: input,
                    });
                    break;
                case 2:
                    setUserinfo({
                        ...userinfo,
                        intro: input,
                    });
                    break;
            }
            setStep(step + 1);
            setInput("");
            inputRef.current.clear();
            setActivated(false);
        } else {
            setStep(0);
            console.log(userinfo);
        }
    };

    return (
        <KeyboardAvoidingView style={styles.rootContainer}>
            <View style={styles.top}>
                <BackButton />
                <SkipButton />
            </View>
            <View style={styles.indicator}>
                <Indicator total={4} active={step} />
            </View>
            <Text style={styles.guide}>{guideSet[step]}</Text>
            <View style={{ flex: 1 }}>
                <TextInput
                    style={styles.input}
                    placeholder={placeholderSet[step]}
                    placeholderTextColor={theme.grey3}
                    onChange={(text) => onChange(text)}
                    ref={inputRef}
                />
            </View>
            <View style={{ alignItems: "center", marginBottom: "20px" }}>
                <NextButton onPress={onPress} isActivate={activated} />
            </View>
        </KeyboardAvoidingView>
    );
}
const styles = StyleSheet.create({
    rootContainer: {
        flex: 1,
    },
    top: {
        height: "48px",
        flexDirection: "row",
        alignItems: "center",
        justifyContent: "space-between",
        paddingHorizontal: "20px",
    },
    indicator: {
        height: "48px",
        justifyContent: "center",
        paddingStart: "20px",
    },
    guide: {
        fontSize: "24px",
        color: theme.grey5,
        fontWeight: "700",
        marginLeft: "20px",
    },
    input: {
        fontSize: "24px",
        fontWeight: "700",
        marginTop: "50px",
        marginHorizontal: "20px",
    },
});

export default InitUserInfo;
