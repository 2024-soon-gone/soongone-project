import {
  useCameraPermission,
  useCameraDevice,
  Camera,
} from 'react-native-vision-camera';
import { View, StyleSheet, Text } from 'react-native';
import { useEffect } from 'react';
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
    <Camera style={StyleSheet.absoluteFill} device={device} isActive={true} />
  );
}
export default CameraScreen;
