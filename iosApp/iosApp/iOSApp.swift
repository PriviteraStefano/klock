import SwiftUI
import Shared

@main
struct CounterApp: App {
    var body: some Scene {
        WindowGroup {
            CounterView()
        }
    }
}

struct CounterView: View {
    @State private var viewModel = CounterViewModel()
    @State private var count: Int32 = 0
    
    var body: some View {
        VStack(spacing: 20) {
            Text("Counter App")
                .font(.largeTitle)
                .fontWeight(.bold)
            
            Text("\(count)")
                .font(.system(size: 60))
                .fontWeight(.bold)
                .foregroundColor(.blue)
            
            HStack(spacing: 15) {
                Button(action: {
                    viewModel.decrement()
                    count = viewModel.getCount()
                }) {
                    Text("−")
                        .font(.title)
                        .frame(width: 60, height: 60)
                        .background(Color.red)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                }
                
                Button(action: {
                    viewModel.reset()
                    count = viewModel.getCount()
                }) {
                    Text("Reset")
                        .frame(width: 100, height: 60)
                        .background(Color.gray)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                }
                
                Button(action: {
                    viewModel.increment()
                    count = viewModel.getCount()
                }) {
                    Text("+")
                        .font(.title)
                        .frame(width: 60, height: 60)
                        .background(Color.green)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                }
            }
            
            Spacer()
        }
        .padding()
        .onAppear {
            count = viewModel.getCount()
        }
    }
}

#Preview {
    CounterView()
}
