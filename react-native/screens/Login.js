import React, { useEffect } from "react";
import { View, Text, Pressable, StyleSheet } from "react-native";
import { StatusBar } from "expo-status-bar";
import * as Google from "expo-auth-session/providers/google";
import * as WebBrowser from "expo-web-browser";
import { GOOGLE_AUTH_CLIENTID } from "@env";

WebBrowser.maybeCompleteAuthSession();

function Login({ navigation }) {
    const [request, response, promptAsync] = Google.useAuthRequest({
        webClientId: GOOGLE_AUTH_CLIENTID,
    });
    useEffect(() => {
        if (response?.type === "success") {
            const { authentication } = response;
            console.log(authentication);
        }
    }, [response]);
    return (
        <View style={styles.container}>
            <StatusBar style="auto" />
            <Text style={{ marginBottom: 50, fontSize: 20, marginTop: -100 }}>
                Soongone
            </Text>
            <Pressable
                style={({ pressed }) => [
                    { opacity: pressed ? 0.5 : 1.0, ...styles.btn },
                    styles.defaultStyling,
                ]}
                // onPress={() => navigation.navigate("Onboarding")}
                onPress={() => promptAsync()}
            >
                <Text>구글로 로그인</Text>
            </Pressable>
            <Pressable
                style={({ pressed }) => [
                    { opacity: pressed ? 0.5 : 1.0, ...styles.btn },
                    styles.defaultStyling,
                ]}
                onPress={() => navigation.navigate("InitUserInfo")}
            >
                <Text>애플로 로그인</Text>
            </Pressable>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: "#fff",
        alignItems: "center",
        justifyContent: "center",
    },
    input: {
        backgroundColor: "white",
        borderColor: "black",
        borderBottomWidth: 2,
        width: 300,
        marginBottom: 100,
    },
    btn: {
        borderColor: "black",
        borderWidth: 2,
        marginBottom: 10,
        width: 300,
        height: 70,
        alignItems: "center",
        justifyContent: "center",
    },
});

export default Login;
