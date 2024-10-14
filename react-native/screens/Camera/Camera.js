import {
  useCameraPermission,
  useCameraDevice,
  Camera,
} from 'react-native-vision-camera';
import { View, StyleSheet, Text, Pressable } from 'react-native';
import Icon from 'react-native-vector-icons/MaterialIcons';
import { useEffect } from 'react';
import theme from '../../assets/Theme';
function CameraScreen() {
  const device = useCameraDevice('back');
  const { hasPermission, requestPermission } = useCameraPermission();
  useEffect(() => {
    if (!hasPermission) {
      requestPermission();
    }
  }, []);

  if (!hasPermission)
    return (
      <View>
        <Text>권한없음</Text>
      </View>
    );
  if (device == null)
    return (
      <View>
        <Text>카메라 없음</Text>
      </View>
    );
  return (
    <View style={styles.root}>
      <View style={styles.upper}></View>
      <Camera style={StyleSheet.absoluteFill} device={device} isActive={true} />
      <View style={styles.bottom}>
        <Pressable
          style={({ pressed }) => [
            { opacity: pressed ? 0.5 : 1.0 },
            styles.defaultStyling,
          ]}
        >
          <Icon name="circle" size={100} color={theme.white}></Icon>
        </Pressable>
      </View>
    </View>
  );
}
const styles = StyleSheet.create({
  root: { flex: 1 },
  upper: {
    position: 'absolute',
    width: '100%',
    height: 138,
    backgroundColor: 'rgba(0,0,0,0.4)',
    zIndex: 1,
  },
  bottom: {
    position: 'absolute',
    bottom: 0,
    width: '100%',
    height: 200,
    backgroundColor: 'rgba(0,0,0,0.4)',
    zIndex: 1,
    alignItems: 'center',
    paddingTop: 30,
  },
  circle: {},
});
export default CameraScreen;
