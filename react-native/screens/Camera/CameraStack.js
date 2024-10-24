import 'react-native-gesture-handler';
import { createStackNavigator } from '@react-navigation/stack';
import CameraScreen from './Camera';
import MintScreen from './Mint';

export default function CameraStack() {
  const Stack = createStackNavigator();
  return (
    <Stack.Navigator
      initialRouteName="Camera"
      screenOptions={{
        headerShown: false,
        headerTitleAlign: 'center',
      }}
    >
      <Stack.Screen name="Camera" component={CameraScreen} />
      <Stack.Screen name="Mint" component={MintScreen} />
    </Stack.Navigator>
  );
}
